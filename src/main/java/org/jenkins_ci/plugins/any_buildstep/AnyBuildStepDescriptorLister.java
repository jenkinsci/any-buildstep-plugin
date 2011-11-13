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

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.tasks.BuildStep;
import org.jenkins_ci.plugins.conditional_builder.BuilderDescriptorLister;
import org.jenkins_ci.plugins.conditional_builder.DefaultBuilderDescriptorLister;
import org.jenkins_ci.plugins.flexible_publish.DefaultPublisherDescriptorLister;
import org.jenkins_ci.plugins.flexible_publish.PublisherDescriptorLister;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

public class AnyBuildStepDescriptorLister {

    private static List<? extends Descriptor<? extends BuildStep>> getBuildSteps(final AbstractProject<?, ?> project) {
        final DefaultBuilderDescriptorLister builderLister = new DefaultBuilderDescriptorLister();
        final DefaultPublisherDescriptorLister publisherLister = new DefaultPublisherDescriptorLister();
        final List steps = builderLister.getAllowedBuilders(project);
        steps.addAll(publisherLister.getAllowedPublishers(project));
        return steps;
    }

    public static class PublisherLister implements PublisherDescriptorLister {

        @DataBoundConstructor
        public PublisherLister() {
        }

        public List<? extends Descriptor<? extends BuildStep>> getAllowedPublishers(final AbstractProject<?, ?> project) {
            return getBuildSteps(project);
        }

        public PublisherDescriptor getDescriptor() {
            return Hudson.getInstance().getDescriptorByType(PublisherDescriptor.class);
        }

        @Extension
        public static class PublisherDescriptor extends Descriptor<PublisherDescriptorLister> {
            @Override
            public String getDisplayName() {
                return Messages.displayName();
            }
        }
        
    }

    public static class BuilderLister implements BuilderDescriptorLister {

        @DataBoundConstructor
        public BuilderLister() {
        }

        public List<? extends Descriptor<? extends BuildStep>> getAllowedBuilders(final AbstractProject<?, ?> project) {
            return getBuildSteps(project);
        }

        public BuilderDescriptor getDescriptor() {
            return Hudson.getInstance().getDescriptorByType(BuilderDescriptor.class);
        }

        @Extension
        public static class BuilderDescriptor extends Descriptor<BuilderDescriptorLister> {
            @Override
            public String getDisplayName() {
                return Messages.displayName();
            }
        }
        
    }

}
