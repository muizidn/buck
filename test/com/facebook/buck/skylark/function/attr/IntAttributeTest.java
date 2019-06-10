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
package com.facebook.buck.skylark.function.attr;

import static org.junit.Assert.assertEquals;

import com.facebook.buck.core.cell.CellPathResolver;
import com.facebook.buck.core.cell.TestCellPathResolver;
import com.facebook.buck.core.model.EmptyTargetConfiguration;
import com.facebook.buck.io.filesystem.impl.FakeProjectFilesystem;
import com.facebook.buck.rules.coercer.CoerceFailedException;
import com.google.common.collect.ImmutableList;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IntAttributeTest {

  private final FakeProjectFilesystem filesystem = new FakeProjectFilesystem();
  private final CellPathResolver cellRoots = TestCellPathResolver.get(filesystem);

  @Rule public ExpectedException expected = ExpectedException.none();

  @Test
  public void coercesIntegersProperly() throws CoerceFailedException {

    IntAttribute attr = new ImmutableIntAttribute(4, "", true, ImmutableList.of());
    Integer coerced =
        attr.getValue(cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 5);
    Optional<Integer> coercedPresent =
        attr.getOptionalValue(
            cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 5);
    Optional<Integer> coercedMissing =
        attr.getOptionalValue(
            cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, null);

    assertEquals(5, coerced.intValue());
    assertEquals(Optional.of(5), coercedPresent);
    assertEquals(Optional.empty(), coercedMissing);
  }

  @Test
  public void failsMandatoryCoercionProperly() throws CoerceFailedException {
    expected.expect(CoerceFailedException.class);

    IntAttribute attr = new ImmutableIntAttribute(4, "", true, ImmutableList.of());

    attr.getValue(cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, "foo");
  }

  @Test
  public void failsOptionalCoercionProperly() throws CoerceFailedException {
    expected.expect(CoerceFailedException.class);

    IntAttribute attr = new ImmutableIntAttribute(4, "", true, ImmutableList.of());

    attr.getOptionalValue(
        cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, "foo");
  }

  @Test
  public void succeedsIfValueInArray() throws CoerceFailedException {

    IntAttribute attr = new ImmutableIntAttribute(4, "", true, ImmutableList.of(1, 2, 3, 4));
    int value =
        attr.getValue(cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 3);
    Optional<Integer> optionalValue =
        attr.getOptionalValue(
            cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 3);
    Optional<Integer> optionalValueMissing =
        attr.getOptionalValue(
            cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, null);

    assertEquals(3, value);
    assertEquals(Optional.of(3), optionalValue);
    assertEquals(Optional.empty(), optionalValueMissing);
  }

  @Test
  public void allowsAnyValueIfValuesIsEmptyList() throws CoerceFailedException {
    IntAttribute attr = new ImmutableIntAttribute(4, "", true, ImmutableList.of());
    int value =
        attr.getValue(cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 3);
    Optional<Integer> optionalValue =
        attr.getOptionalValue(
            cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 3);
    Optional<Integer> optionalValueMissing =
        attr.getOptionalValue(
            cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, null);

    assertEquals(3, value);
    assertEquals(Optional.of(3), optionalValue);
    assertEquals(Optional.empty(), optionalValueMissing);
  }

  @Test
  public void failsIfValueNotInArray() throws CoerceFailedException {
    expected.expect(CoerceFailedException.class);
    expected.expectMessage("must be one of '1', '2', '4' instead of '3'");

    IntAttribute attr = new ImmutableIntAttribute(4, "", true, ImmutableList.of(1, 2, 4));

    attr.getValue(cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 3);
  }

  @Test
  public void optionalFailsIfValueNotInArray() throws CoerceFailedException {

    IntAttribute attr = new ImmutableIntAttribute(4, "", true, ImmutableList.of(1, 2, 4));

    Optional<Integer> optionalValueMissing =
        attr.getOptionalValue(
            cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, null);
    assertEquals(Optional.empty(), optionalValueMissing);

    expected.expect(CoerceFailedException.class);
    expected.expectMessage("must be one of '1', '2', '4' instead of '3'");
    attr.getOptionalValue(
        cellRoots, filesystem, Paths.get(""), EmptyTargetConfiguration.INSTANCE, 3);
  }
}
