/*
 * Copyright 2019-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.buck.core.rules.actions;

import java.nio.file.Path;
import org.immutables.value.Value;

/**
 * The {@link DeclaredArtifact} is the promise during the rule implementation that an {@link Action}
 * will be created to generate an output at the corresponding path.
 *
 * <p>This is not an {@link Artifact} in itself, and cannot be used as such for any {@link Action}
 * lookup, since it is not fully materialized with a corresponding {@link Action}.
 *
 * <p>This {@link DeclaredArtifact} becomes materialized once a corresponding {@link Action} has
 * been created.
 */
@Value.Immutable(builder = false, copy = false)
@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE)
public abstract class DeclaredArtifact {

  @Value.Parameter
  abstract Path getPackagePath();

  @Value.Parameter
  abstract Path getOutputPath();

  Artifact.BuildArtifact materialize(ActionAnalysisDataKey key) {
    return ImmutableBuildArtifact.of(key, key.getBuildTarget(), getPackagePath(), getOutputPath());
  }
}
