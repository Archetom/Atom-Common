package io.github.archetom.common.result;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 分页查询结果
 *
 * @param <T> 结果类
 */
@Getter
public class Pager<T> extends BaseToString {

    private static final long serialVersionUID = -5139677999381928520L;

    /**
     * 无结果总数返回
     */
    public static final long NO_TOTAL_NUM = -1L;

    /**
     * 查询到的结果总数
     */
    private long totalNum = NO_TOTAL_NUM;

    /**
     * 当前页码 《最小值为1 最大值为100》
     */
    @Setter
    private long pageNum = 1;

    /**
     * 每页条数 《最小值为1 最大值为100》
     */
    @Setter
    private long pageSize = 20;

    /**
     * 记录list
     */
    @Setter
    private List<T> objectList;

    /**
     * 分页中的额外数据
     */
    @Setter
    private Map<String, Object> meta;

    /**
     * 传参构造
     *
     * @param objectList 记录list
     * @param pageSize   长度
     * @param pageNum    每页个数
     * @param totalNum   总页数
     * @param meta       其他参数
     */
    public Pager(List<T> objectList, long pageSize, long pageNum, long totalNum, Map<String, Object> meta) {
        this.objectList = objectList;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalNum = totalNum;
        this.meta = meta;
    }

    /**
     * 空构造
     */
    public Pager() {
    }

    /**
     * 设置总数
     *
     * @param totalNum 总数
     */
    public void setTotalNum(final Long totalNum) {
        this.totalNum = Optional.ofNullable(totalNum).orElse(NO_TOTAL_NUM);
    }

    /**
     * 将当前分页对象中的数据类型从T转换为E
     *
     * @param func 转换函数,用于将T类型转换为E类型
     * @param <E>  目标数据类型
     * @return 转换后的新分页对象, 包含E类型的数据列表, 其他分页参数保持不变
     */
    public <E> Pager<E> map(Function<T, ? extends E> func) {
        List<E> recordList = Lists.transform(this.objectList, func::apply);
        return new Pager<>(recordList, this.pageSize, this.pageNum, this.totalNum, this.meta);
    }
}
