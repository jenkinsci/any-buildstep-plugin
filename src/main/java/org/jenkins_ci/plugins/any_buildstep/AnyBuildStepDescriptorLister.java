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
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;
import org.jenkins_ci.plugins.conditional_builder.BuilderDescriptorLister;
import org.jenkins_ci.plugins.conditional_builder.ConditionalBuilder;
import org.jenkins_ci.plugins.flexible_publish.FlexiblePublisher;
import org.jenkins_ci.plugins.flexible_publish.PublisherDescriptorLister;
import org.kohsuke.stapler.DataBoundConstructor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class AnyBuildStepDescriptorLister {

    private static List<BuildStepDescriptor<? extends BuildStep>> getBuildSteps(final AbstractProject<?, ?> project) {
        final List<BuildStepDescriptor<? extends BuildStep>> steps = new ArrayList<BuildStepDescriptor<? extends BuildStep>>();
        if (project == null) return steps;
        final List<Descriptor> potential = new ArrayList<Descriptor>();
        potential.addAll(Builder.all());
        potential.addAll(Publisher.all());
        for (Descriptor descriptor : potential) {
            if (descriptor instanceof FlexiblePublisher.FlexiblePublisherDescriptor) continue;
            if (descriptor instanceof ConditionalBuilder.ConditionalBuilderDescriptor) continue;
            if (!(descriptor instanceof BuildStepDescriptor)) continue;
            BuildStepDescriptor<? extends Publisher> buildStepDescriptor = (BuildStepDescriptor) descriptor;
            if ((project != null) && buildStepDescriptor.isApplicable(project.getClass())) {
                if (hasDbc(buildStepDescriptor.clazz))
                    steps.add(buildStepDescriptor);
            }
        }
        return steps;
    }

    private static boolean hasDbc(final Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.isAnnotationPresent(DataBoundConstructor.class))
                return true;
        }
        return false;
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
