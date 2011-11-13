/*
 * The MIT License
 *
 * Copyright (C) 2011 by Anthony Robinson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jenkins_ci.plugins.any_buildstep;

import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildStep;
import org.jenkins_ci.plugins.flexible_publish.DefaultPublisherDescriptorLister;
import org.jenkinsci.plugins.conditionalbuildstep.singlestep.DefaultBuilderDescriptorLister;

import java.util.List;

public class AnyBuildStepDescriptorLister {

    public static List<? extends Descriptor<? extends BuildStep>> getBuildSteps(final AbstractProject<?, ?> project) {
        final DefaultBuilderDescriptorLister builderLister = new DefaultBuilderDescriptorLister();
        final DefaultPublisherDescriptorLister publisherLister = new DefaultPublisherDescriptorLister();
        final List steps = builderLister.getAllowedBuilders(project);
        steps.addAll(publisherLister.getAllowedPublishers(project));
        return steps;
    }

}
