package io.github.archetom.common.result;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class PagerTest {

    @Test
    void usesNoTotalNumForTheDefaultAndNullSetterValues() {
        Pager<String> pager = new Pager<>();

        assertEquals(Pager.NO_TOTAL_NUM, pager.getTotalNum());

        pager.setTotalNum(null);

        assertEquals(Pager.NO_TOTAL_NUM, pager.getTotalNum());
    }

    @Test
    void mapPreservesAllPagingFieldsIncludingTheNoTotalNumSentinelAndMeta() {
        Map<String, Object> meta = Map.of("source", "contract-test");
        Pager<String> pager = new Pager<>(List.of("10", "20"), 25, 3, Pager.NO_TOTAL_NUM, meta);

        Pager<Integer> mapped = pager.map(Integer::valueOf);

        assertAll(
                () -> assertEquals(3, mapped.getPageNum()),
                () -> assertEquals(25, mapped.getPageSize()),
                () -> assertEquals(Pager.NO_TOTAL_NUM, mapped.getTotalNum()),
                () -> assertEquals(List.of(10, 20), mapped.getObjectList()),
                () -> assertSame(meta, mapped.getMeta())
        );
    }
}
