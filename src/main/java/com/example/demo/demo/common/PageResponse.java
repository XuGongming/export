package com.example.demo.demo.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author xugm
 * @create 2022/1/11 9:27
 */
public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = -1816325636807144628L;
    private Number total;
    private List<T> list;

    private PageResponse() {
    }

    private PageResponse(Number total, List<T> data) {
        this.total = BigInteger.valueOf(Objects.isNull(total) ? 0L : total.longValue());
        this.list = data != null && !data.isEmpty() ? data : Collections.emptyList();
    }

    @JsonIgnore
    public boolean isEmpty() {
        return Objects.isNull(this.total) || Objects.equals(this.total.longValue(), 0L);
    }

    public static <E> PageResponse<E> empty() {
        return new PageResponse(BigInteger.valueOf(0L), Collections.emptyList());
    }

    public static <E> PageResponse<E> apply(Number total, List<E> data) {
        BigInteger count = BigInteger.valueOf(total.longValue() < 0L ? 0L : total.longValue());
        return new PageResponse(total, data);
    }

    public Number getTotal() {
        return this.total;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setTotal(final Number total) {
        this.total = total;
    }

    public void setList(final List<T> list) {
        this.list = list;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageResponse)) {
            return false;
        } else {
            PageResponse<?> other = (PageResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$total = this.getTotal();
                Object other$total = other.getTotal();
                if (this$total == null) {
                    if (other$total != null) {
                        return false;
                    }
                } else if (!this$total.equals(other$total)) {
                    return false;
                }

                Object this$list = this.getList();
                Object other$list = other.getList();
                if (this$list == null) {
                    if (other$list != null) {
                        return false;
                    }
                } else if (!this$list.equals(other$list)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PageResponse;
    }



    public String toString() {
        return "PageResponse(total=" + this.getTotal() + ", list=" + this.getList() + ")";
    }
}