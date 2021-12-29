package com.github.www.hermes.protocol;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class HeartbeatRequest extends AbstractRequest {

    private String msg;

}
