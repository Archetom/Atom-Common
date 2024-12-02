package io.github.archetom.common.result;

import com.google.common.base.MoreObjects;

import java.io.Serializable;


/**
 * toString方法包装类，包装toString()方法。
 */
public abstract class BaseToString implements Serializable {

    private static final long serialVersionUID = -1010969599219896835L;

    /**
     * 重载toString方法
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("class", this.getClass().getSimpleName())
                .add("hashCode", System.identityHashCode(this))
                .toString();
    }
}
