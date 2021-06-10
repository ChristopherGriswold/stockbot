package com.iceybones.capstone.dl4j;

import java.util.concurrent.atomic.AtomicLong;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.deeplearning4j.exception.DL4JInvalidInputException;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.api.BaseTrainingListener;
import org.deeplearning4j.optimize.api.InvocationType;
import org.nd4j.evaluation.IEvaluation;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;

public class EvaluativeListener extends BaseTrainingListener {

  protected transient ThreadLocal<AtomicLong> iterationCount = new ThreadLocal<>();
  protected int frequency;

  protected AtomicLong invocationCount = new AtomicLong(0);

  protected transient DataSetIterator dsIterator;
  protected transient MultiDataSetIterator mdsIterator;
  protected DataSet ds;
  protected MultiDataSet mds;
  protected ListView<String> testNewsParent;

  @Getter
  protected IEvaluation[] evaluations;

  @Getter
  protected InvocationType invocationType;

  /**
   * This callback will be invoked after evaluation finished
   */
  @Getter
  @Setter
  protected transient EvaluationCallback callback;

  private interface EvaluationCallback {

    void call(EvaluativeListener listener, Model model, long invocationsCount,
        IEvaluation[] evaluations);
  }

  /**
   * Evaluation will be launched after each *frequency* iterations, with {@link Evaluation}
   * datatype
   *
   * @param iterator  Iterator to provide data for evaluation
   * @param frequency Frequency (in number of iterations) to perform evaluation
   */
  public EvaluativeListener(@NonNull DataSetIterator iterator, int frequency) {
    this(iterator, frequency, InvocationType.ITERATION_END, new Evaluation());
  }

  /**
   * @param iterator  Iterator to provide data for evaluation
   * @param frequency Frequency (in number of iterations/epochs according to the invocation type) to
   *                  perform evaluation
   * @param type      Type of value for 'frequency' - iteration end, epoch end, etc
   */
  public EvaluativeListener(ListView<String> newsParent, @NonNull DataSetIterator iterator,
      int frequency,
      @NonNull InvocationType type) {
    this(iterator, frequency, type, new Evaluation());
    testNewsParent = newsParent;
  }

  /**
   * Evaluation will be launched after each *frequency* iterations, with {@link Evaluation}
   * datatype
   *
   * @param iterator  Iterator to provide data for evaluation
   * @param frequency Frequency (in number of iterations) to perform evaluation
   */
  public EvaluativeListener(@NonNull MultiDataSetIterator iterator, int frequency) {
    this(iterator, frequency, InvocationType.ITERATION_END, new Evaluation());
  }

  /**
   * @param iterator  Iterator to provide data for evaluation
   * @param frequency Frequency (in number of iterations/epochs according to the invocation type) to
   *                  perform evaluation
   * @param type      Type of value for 'frequency' - iteration end, epoch end, etc
   */
  public EvaluativeListener(@NonNull MultiDataSetIterator iterator, int frequency,
      @NonNull InvocationType type) {
    this(iterator, frequency, type, new Evaluation());
  }

  /**
   * Evaluation will be launched after each *frequency* iteration
   *
   * @param iterator    Iterator to provide data for evaluation
   * @param frequency   Frequency (in number of iterations) to perform evaluation
   * @param evaluations Type of evalutions to perform
   */
  public EvaluativeListener(@NonNull DataSetIterator iterator, int frequency,
      IEvaluation... evaluations) {
    this(iterator, frequency, InvocationType.ITERATION_END, evaluations);
  }

  /**
   * Evaluation will be launched after each *frequency* iteration
   *
   * @param iterator    Iterator to provide data for evaluation
   * @param frequency   Frequency (in number of iterations/epochs according to the invocation type)
   *                    to perform evaluation
   * @param type        Type of value for 'frequency' - iteration end, epoch end, etc
   * @param evaluations Type of evalutions to perform
   */
  public EvaluativeListener(@NonNull DataSetIterator iterator, int frequency,
      @NonNull InvocationType type,
      IEvaluation... evaluations) {
    this.dsIterator = iterator;
    this.frequency = frequency;
    this.evaluations = evaluations;

    this.invocationType = type;
  }

  /**
   * Evaluation will be launched after each *frequency* iteration
   *
   * @param iterator    Iterator to provide data for evaluation
   * @param frequency   Frequency (in number of iterations) to perform evaluation
   * @param evaluations Type of evalutions to perform
   */
  public EvaluativeListener(@NonNull MultiDataSetIterator iterator, int frequency,
      IEvaluation... evaluations) {
    this(iterator, frequency, InvocationType.ITERATION_END, evaluations);
  }

  /**
   * Evaluation will be launched after each *frequency* iteration
   *
   * @param iterator    Iterator to provide data for evaluation
   * @param frequency   Frequency (in number of iterations/epochs according to the invocation type)
   *                    to perform evaluation
   * @param type        Type of value for 'frequency' - iteration end, epoch end, etc
   * @param evaluations Type of evalutions to perform
   */
  public EvaluativeListener(@NonNull MultiDataSetIterator iterator, int frequency,
      @NonNull InvocationType type,
      IEvaluation... evaluations) {
    this.mdsIterator = iterator;
    this.frequency = frequency;
    this.evaluations = evaluations;

    this.invocationType = type;
  }

  public EvaluativeListener(@NonNull DataSet dataSet, int frequency, @NonNull InvocationType type) {
    this(dataSet, frequency, type, new Evaluation());
  }

  public EvaluativeListener(@NonNull MultiDataSet multiDataSet, int frequency,
      @NonNull InvocationType type) {
    this(multiDataSet, frequency, type, new Evaluation());
  }

  public EvaluativeListener(@NonNull DataSet dataSet, int frequency, @NonNull InvocationType type,
      IEvaluation... evaluations) {
    this.ds = dataSet;
    this.frequency = frequency;
    this.evaluations = evaluations;

    this.invocationType = type;
  }

  public EvaluativeListener(@NonNull MultiDataSet multiDataSet, int frequency,
      @NonNull InvocationType type,
      IEvaluation... evaluations) {
    this.mds = multiDataSet;
    this.frequency = frequency;
    this.evaluations = evaluations;

    this.invocationType = type;
  }

  /**
   * Event listener for each iteration
   *
   * @param model     the model iterating
   * @param iteration the iteration
   */
  @Override
  public void iterationDone(Model model, int iteration, int epoch) {
    if (invocationType == InvocationType.ITERATION_END) {
      invokeListener(model);
    }
  }

  @Override
  public void onEpochStart(Model model) {
    if (invocationType == InvocationType.EPOCH_START) {
      invokeListener(model);
    }
  }

  @Override
  public void onEpochEnd(Model model) {
    if (invocationType == InvocationType.EPOCH_END) {
      invokeListener(model);
    }
  }

  protected void invokeListener(Model model) {
    if (iterationCount.get() == null) {
      iterationCount.set(new AtomicLong(0));
    }

    if (iterationCount.get().getAndIncrement() % frequency != 0) {
      return;
    }

    for (IEvaluation evaluation : evaluations) {
      evaluation.reset();
    }

    if (dsIterator != null && dsIterator.resetSupported()) {
      dsIterator.reset();
    } else if (mdsIterator != null && mdsIterator.resetSupported()) {
      mdsIterator.reset();
    }

    // FIXME: we need to save/restore inputs, if we're being invoked with iterations > 1

    Platform.runLater(() -> testNewsParent.getItems()
        .add("Starting evaluation No. " + invocationCount.incrementAndGet()));
    if (model instanceof MultiLayerNetwork) {
      if (dsIterator != null) {
        ((MultiLayerNetwork) model).doEvaluation(dsIterator, evaluations);
      } else if (ds != null) {
        for (IEvaluation evaluation : evaluations) {
          evaluation.eval(ds.getLabels(), ((MultiLayerNetwork) model).output(ds.getFeatures()));
        }
      }
    } else if (model instanceof ComputationGraph) {
      if (dsIterator != null) {
        ((ComputationGraph) model).doEvaluation(dsIterator, evaluations);
      } else if (mdsIterator != null) {
        ((ComputationGraph) model).doEvaluation(mdsIterator, evaluations);
      } else if (ds != null) {
        for (IEvaluation evaluation : evaluations) {
          evalAtIndex(evaluation, new INDArray[]{ds.getLabels()},
              ((ComputationGraph) model).output(ds.getFeatures()), 0);
        }
      } else if (mds != null) {
        for (IEvaluation evaluation : evaluations) {
          evalAtIndex(evaluation, mds.getLabels(),
              ((ComputationGraph) model).output(mds.getFeatures()), 0);
        }
      }
    } else {
      throw new DL4JInvalidInputException(
          "Model is unknown: " + model.getClass().getCanonicalName());
    }

    // TODO: maybe something better should be used here?
    Platform.runLater(() -> testNewsParent.getItems().add("Reporting evaluation results:"));
    for (IEvaluation evaluation : evaluations) {
      Platform.runLater(() -> {
        testNewsParent.getItems().add(evaluation.getClass().getSimpleName() + ":");
        testNewsParent.getItems().addAll(evaluation.stats().split("\n"));
      });
    }

    if (callback != null) {
      callback.call(this, model, invocationCount.get(), evaluations);
    }
  }

  protected void evalAtIndex(IEvaluation evaluation, INDArray[] labels, INDArray[] predictions,
      int index) {
    evaluation.eval(labels[index], predictions[index]);
  }

}
