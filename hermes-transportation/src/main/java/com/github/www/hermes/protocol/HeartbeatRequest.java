package com.github.www.hermes.protocol;

import com.github.www.hermes.common.ApiKeys;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Stiles yu
 * @since 1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HeartbeatRequest extends AbstractRequest {
    private Date at;


    public HeartbeatRequest(Date at) {
        this.at = at;
        super.code(ApiKeys.HEARTBEAT);
        super.version(ApiKeys.HEARTBEAT);
    }
}
