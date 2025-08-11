// ===== 툴팁 설명 데이터 =====
const STAT_DICT = {
    "AVG":"타율 (H/AB)","G":"경기수","PA":"타석","AB":"타수","R":"득점","H":"안타",
    "2B":"2루타","3B":"3루타","HR":"홈런","TB":"총루타","RBI":"타점","SAC":"희생번트","SF":"희생플라이",
    "BB":"볼넷","IBB":"고의4구","HBP":"사구","SO":"삼진","GDP":"병살타",
    "SLG":"장타율 (TB/AB)","OBP":"출루율","OPS":"출루+장타",
    "MH":"멀티히트(한 경기 2안타+)","RISP":"득점권 타율","PH-BA":"대타 타율"
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
    const statKey = th.getAttribute('data-stat');
    const tipText = STAT_DICT[statKey] || '';

    th.removeAttribute('title'); // 기본 브라우저 툴팁 제거

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

// ===== 터치 스와이프 (가로 스크롤 보호) =====
const scrollContainer = document.querySelector('.table-scroll');
let touchStartX = null;
let touchStartScrollLeft = null;

scrollContainer.addEventListener('touchstart', (e) => {
    touchStartX = e.touches[0].clientX;
    touchStartScrollLeft = scrollContainer.scrollLeft;
}, {passive:true});

scrollContainer.addEventListener('touchmove', (e) => {
    if (touchStartX === null) return;
    const dx = e.touches[0].clientX - touchStartX;
    const movedScroll = Math.abs(scrollContainer.scrollLeft - touchStartScrollLeft);

    // 스크롤을 하고 있다면 그룹 전환 안 함
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
}, {passive:true});

scrollContainer.addEventListener('touchend', () => {
    touchStartX = null;
});

// ===== 초기 실행 =====
applyView();
updateRanks();
