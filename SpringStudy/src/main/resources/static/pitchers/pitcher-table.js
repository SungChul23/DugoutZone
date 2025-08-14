// ===== 툴팁 설명 데이터 (투수) =====
const STAT_PITCH = {
    // 그룹 1
    "ERA": "평균자책점 (ER×9/IP)",
    "G": "등판 경기수",
    "W": "승",
    "L": "패",
    "SV": "세이브",
    "HLD": "홀드",
    "WPCT": "승률",
    "IP": "이닝(투구)",
    "H": "피안타",
    "HR": "피홈런",
    "BB": "볼넷",
    "HBP": "사구",
    "SO": "탈삼진",
    "R": "실점",
    "ER": "자책점",
    "WHIP": "이닝당 출루허용률",
    // 그룹 2
    "CG": "완투",
    "SHO": "완봉승",
    "QS": "퀄리티스타트(6IP, 3ER 이하)",
    "BSV": "블론 세이브",
    "TBF": "상대 타자 수",
    "NP": "투구 수",
    "AVG": "피안타율",
    "2B": "2루타 허용",
    "3B": "3루타 허용",
    "SAC": "희생번트 허용",
    "SF": "희생플라이 허용",
    "IBB": "고의4구",
    "WP": "폭투",
    "BK": "보크"
};


// ===== 순위 다시 매기기 =====
function updateRanks() {
    const rows = document.querySelectorAll('#statsTable tbody tr');
    rows.forEach((tr, i) => {
        const rankCell = tr.querySelector('.rank-cell');
        if (rankCell) rankCell.textContent = i + 1;
    });
}

// ===== 컬럼 클릭 시 클라이언트 정렬 =====
document.querySelectorAll('.stat-header').forEach(header => {
    header.addEventListener('click', () => {
        const stat = header.getAttribute('data-stat');
        const sortKey = header.getAttribute('data-sort') || stat;
        if (!stat) return;

        const table = document.getElementById('statsTable');
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));
        const isAsc = header.classList.toggle('asc');

        rows.sort((a, b) => {
            const aVal = a.querySelector(`[data-stat="${stat}"]`)?.getAttribute('data-value') || '';
            const bVal = b.querySelector(`[data-stat="${stat}"]`)?.getAttribute('data-value') || '';
            const aNum = parseFloat(aVal);
            const bNum = parseFloat(bVal);

            if (!isNaN(aNum) && !isNaN(bNum)) {
                return isAsc ? aNum - bNum : bNum - aNum;
            }
            return isAsc ? aVal.localeCompare(bVal, 'ko') : bVal.localeCompare(aVal, 'ko');
        });

        rows.forEach(r => tbody.appendChild(r));
        updateRanks();
    });
});

// ===== 네온 툴팁 (마우스 위치 기반) =====
let tooltip = document.createElement('div');
tooltip.className = 'custom-tooltip';
document.body.appendChild(tooltip);

const tooltipStyle = tooltip.style;
tooltipStyle.position = 'fixed';
tooltipStyle.padding = '6px 10px';
tooltipStyle.fontSize = '14px';
tooltipStyle.background = 'rgba(10,14,24,0.95)';
tooltipStyle.color = '#e2e8f0';
tooltipStyle.border = '1px solid rgba(0,240,255,0.4)';
tooltipStyle.borderRadius = '6px';
tooltipStyle.pointerEvents = 'none';
tooltipStyle.visibility = 'hidden';
tooltipStyle.zIndex = '9999';

document.querySelectorAll('.th-tip').forEach(th => {
    const key = th.getAttribute('data-stat');
    const tipText = STAT_PITCH[key] || '';

    th.removeAttribute('title');

    th.addEventListener('mouseenter', () => {
        if (tipText) {
            tooltip.textContent = tipText;
            tooltip.style.visibility = 'visible';
        }
    });

    th.addEventListener('mousemove', (e) => {
        tooltip.style.left = (e.clientX + 12) + 'px';
        tooltip.style.top = (e.clientY + 12) + 'px';
    });

    th.addEventListener('mouseleave', () => {
        tooltip.style.visibility = 'hidden';
    });
});

// ===== 그룹 슬라이드 =====
let currentView = 1;
const totalViews = 2;

function applyView() {
    document.querySelectorAll('.group-1').forEach(col => col.style.display = (currentView === 1) ? '' : 'none');
    document.querySelectorAll('.group-2').forEach(col => col.style.display = (currentView === 2) ? '' : 'none');
    document.querySelectorAll('.dot').forEach(dot => {
        dot.classList.toggle('active', parseInt(dot.dataset.view) === currentView);
    });
}

document.getElementById('prevView')?.addEventListener('click', () => {
    currentView = (currentView - 1 < 1) ? totalViews : currentView - 1;
    applyView();
});
document.getElementById('nextView')?.addEventListener('click', () => {
    currentView = (currentView % totalViews) + 1;
    applyView();
});
document.querySelectorAll('.dot').forEach(dot => {
    dot.addEventListener('click', () => {
        currentView = parseInt(dot.dataset.view);
        applyView();
    });
});

// ===== 스와이프 보호 =====
const scrollContainer = document.querySelector('.table-scroll');
let touchStartX = null;
let touchStartScrollLeft = null;

scrollContainer.addEventListener('touchstart', (e) => {
    touchStartX = e.touches[0].clientX;
    touchStartScrollLeft = scrollContainer.scrollLeft;
}, {passive: true});

scrollContainer.addEventListener('touchmove', (e) => {
    if (touchStartX === null) return;
    const dx = e.touches[0].clientX - touchStartX;
    const movedScroll = Math.abs(scrollContainer.scrollLeft - touchStartScrollLeft);

    if (movedScroll > 5) return;

    if (Math.abs(dx) > 50 && movedScroll <= 5) {
        if (dx < 0) {
            currentView = (currentView % totalViews) + 1;
        } else {
            currentView = (currentView - 1 < 1) ? totalViews : currentView - 1;
        }
        applyView();
        touchStartX = null;
    }
}, {passive: true});

scrollContainer.addEventListener('touchend', () => {
    touchStartX = null;
});

// 초기 실행
applyView();
updateRanks();
