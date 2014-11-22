/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.orca.kato.tasks

import groovy.transform.CompileStatic
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.orca.DefaultTaskResult
import com.netflix.spinnaker.orca.ExecutionStatus
import com.netflix.spinnaker.orca.Task
import com.netflix.spinnaker.orca.TaskResult
import com.netflix.spinnaker.orca.kato.api.ops.ResizeAsgOperation
import com.netflix.spinnaker.orca.pipeline.model.ImmutableStage
import org.springframework.beans.factory.annotation.Autowired
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES

@CompileStatic
class PreconfigureDestroyAsgTask implements Task {

  @Autowired
  ObjectMapper mapper

  @Override
  TaskResult execute(ImmutableStage stage) {
    def op = convert(stage)
    new DefaultTaskResult(ExecutionStatus.SUCCEEDED, [
      "resizeAsg.credentials"     : op.credentials,
      "resizeAsg.regions"         : op.regions,
      "resizeAsg.asgName"         : op.asgName,
      "resizeAsg.capacity.min"    : 0,
      "resizeAsg.capacity.max"    : 0,
      "resizeAsg.capacity.desired": 0
    ])
  }

  ResizeAsgOperation convert(ImmutableStage stage) {
    mapper.copy()
          .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
          .convertValue(stage.context, ResizeAsgOperation)
  }
}
