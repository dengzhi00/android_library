package com.dzm.banner.holder;

import java.util.List;

/**
 * @author 邓治民
 *         data 2018/2/27 下午5:53
 */

public interface DzmViewHolder<T> {

    Holder<T> createHolder();

    List<T> getDatas();

}
