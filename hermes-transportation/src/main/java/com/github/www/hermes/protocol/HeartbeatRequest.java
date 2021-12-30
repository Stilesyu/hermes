package com.github.www.hermes.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HeartbeatRequest extends AbstractRequest {
    private Date at;
}
