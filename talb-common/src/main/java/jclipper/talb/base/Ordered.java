package jclipper.talb.base;

/**
 * 顺序
 *
 * @author <a href="mailto:wf2311@163.com">wf2311</a>
 * @since 2022/1/13 09:53.
 */
public interface Ordered {

    /**
     * Useful constant for the highest precedence value.
     *
     * @see java.lang.Integer#MIN_VALUE
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * Useful constant for the lowest precedence value.
     *
     * @see java.lang.Integer#MAX_VALUE
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
}
