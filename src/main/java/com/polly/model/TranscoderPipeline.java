package com.polly.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pradeep_ga
 *
 */
public class TranscoderPipeline {

    List<Pipeline> pipelines = new ArrayList<>();

    /**
     * @return - List of Amazon Transcoder Pipelines
     */
    public List<Pipeline> getPipelines() {
        return pipelines;
    }

    /**
     * @param pipelines - sets list of Amazon Transcoder Pipelines
     */
    public void setPipelines(List<Pipeline> pipelines) {
        this.pipelines = pipelines;
    }
}
