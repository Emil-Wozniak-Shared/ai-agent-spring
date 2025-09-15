package pl.ejdev.agent.infrastructure.openai.port.`in`

import pl.ejdev.agent.infrastructure.base.port.Port
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserEvent
import pl.ejdev.agent.infrastructure.openai.dto.DescribeUserResult

interface DescribeUserPort : Port<DescribeUserEvent, DescribeUserResult>