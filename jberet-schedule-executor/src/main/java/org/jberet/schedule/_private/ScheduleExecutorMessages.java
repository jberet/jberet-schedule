/*
 * Copyright (c) 2014 Red Hat, Inc. and/or its affiliates.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.jberet.schedule._private;

import jakarta.batch.operations.BatchRuntimeException;

import org.jberet.schedule.JobScheduleConfig;
import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.jboss.logging.annotations.ValidIdRange;

@MessageBundle(projectCode = "JBERET")
@ValidIdRange(min = 72000, max = 72499)
public interface ScheduleExecutorMessages {
    ScheduleExecutorMessages MESSAGES = Messages.getBundle(ScheduleExecutorMessages.class);

    @Message(id = 72000, value = "Failed to create JobScheduler of type %s")
    BatchRuntimeException failToCreateJobScheduler(@Cause Throwable th, Class<?> schedulerType);

    @Message(id = 72001, value = "Invalid job schedule config %s")
    BatchRuntimeException invalidJobScheduleConfig(JobScheduleConfig config);

}
