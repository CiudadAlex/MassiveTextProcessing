package org.leviatan.machinelearning.massivetextprocessing.util.platform;

import java.util.Date;

/**
 * TimeProgressLogger.
 *
 * @author acc
 *
 */
public class TimeProgressLogger {

    private final Date initDate;
    private final int totalSteps;
    private final int stepsToPrint;

    private int stepsFinished;

    /**
     * Constructor for TimeProgressLogger.
     *
     * @param totalSteps
     *            totalSteps
     */
    public TimeProgressLogger(final int totalSteps) {
        this(totalSteps, 1);
    }

    /**
     * Constructor for TimeProgressLogger.
     *
     * @param totalSteps
     *            totalSteps
     * @param stepsToPrint
     *            stepsToPrint
     */
    public TimeProgressLogger(final int totalSteps, final int stepsToPrint) {
        this.initDate = new Date();
        this.totalSteps = totalSteps;
        this.stepsToPrint = stepsToPrint;

        this.stepsFinished = 0;
    }

    /** Adds 1 to the stepsFinished and prints progress. */
    public synchronized void stepFinishedAndPrintProgress() {

        this.stepsFinished++;

        if (shouldPrint()) {
            printProgress();
        }
    }

    private boolean shouldPrint() {

        if (this.stepsFinished == 1) {
            return true;
        }

        if (this.stepsFinished == this.totalSteps) {
            return true;
        }

        if (this.stepsFinished % this.stepsToPrint == 0) {
            return true;
        }

        return false;
    }

    private void printProgress() {

        final Date now = new Date();
        final Date estimateFinishTime = estimateFinishTime(this.stepsFinished, now);

        final String timeRunning = TimeUtils.toStringTimeLapse(this.initDate, now);
        final String timeToGo = TimeUtils.toStringTimeLapse(now, estimateFinishTime);
        final double stepsProgress = (double) this.stepsFinished / (double) this.totalSteps;

        final StringBuilder sb = new StringBuilder();

        sb.append("\n-------------------------------\n");
        sb.append("Time Running = ").append(timeRunning).append("\n");
        sb.append("Time To Go   = ").append(timeToGo).append("\n");
        sb.append("Progress     = ").append(100 * stepsProgress).append("%   ").append(this.stepsFinished).append("/")
                .append(this.totalSteps).append("\n");
        sb.append("-------------------------------\n");

        AppLogger.logDebug(sb.toString());
    }

    private Date estimateFinishTime(final int stepsFinished, final Date evaluatingTime) {

        final double stepsProgress = (double) stepsFinished / (double) this.totalSteps;
        final double millisPassed = evaluatingTime.getTime() - this.initDate.getTime();

        final double totalTime = millisPassed / stepsProgress;

        return new Date(this.initDate.getTime() + (long) totalTime);
    }
}
