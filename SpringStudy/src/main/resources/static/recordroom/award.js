(function () {
    // 숫자만 추출: ".355", "35", "102" 등에서 실수 변환
    function parseNumeric(val) {
        if (val == null) return NaN;
        const s = String(val).replace(/[^0-9.\-]/g, '');
        const num = parseFloat(s);
        return isNaN(num) ? NaN : num;
    }

    // 각 카드(section) 단위로 최댓값 찾고, %로 바 너비 설정
    document.querySelectorAll('.award-card').forEach(card => {
        const bars = Array.from(card.querySelectorAll('.bar'));
        if (bars.length === 0) return;

        const values = bars.map(b => parseNumeric(b.getAttribute('data-value')));
        const max = Math.max(...values.filter(v => !isNaN(v)));

        bars.forEach((bar, idx) => {
            const v = values[idx];
            if (isNaN(v) || !isFinite(max) || max <= 0) {
                bar.style.width = '0%';
                return;
            }
            // 2~5위가 모두 0이면 밋밋하니 최소 표시폭 보정(최대 6%)
            const pct = Math.max(6, (v / max) * 100);
            bar.style.width = (pct > 100 ? 100 : pct) + '%';
        });
    });
})();
