// ===== 툴팁 텍스트 =====
const STAT_DICT = {
    "AVG":"타율 (H/AB)",
    "G":"경기수","PA":"타석","AB":"타수","R":"득점","H":"안타",
    "2B":"2루타","3B":"3루타","HR":"홈런","TB":"총루타","RBI":"타점",
    "SAC":"희생번트","SF":"희생플라이",
    "BB":"볼넷","IBB":"고의4구","HBP":"사구","SO":"삼진","GDP":"병살타",
    "SLG":"장타율 (TB/AB)","OBP":"출루율","OPS":"출루+장타",
    "MH":"멀티히트(한 경기 2안타+)","RISP":"득점권 타율","PH-BA":"대타 타율"
};

// ===== 툴팁 표시 =====
document.querySelectorAll('.th-tip').forEach(th => {
    const statKey = th.getAttribute('data-stat');
    const tipText = STAT_DICT[statKey] || '';
    const tipBox = th.querySelector('.tip-box');

    if (tipBox && tipText) {
        tipBox.textContent = tipText;
    }

    // 마우스 오버 시 위쪽에 표시
    th.addEventListener('mouseenter', () => {
        if (tipBox && tipText) {
            tipBox.style.visibility = 'visible';
            tipBox.style.opacity = '1';
        }
    });
    th.addEventListener('mouseleave', () => {
        if (tipBox) {
            tipBox.style.visibility = 'hidden';
            tipBox.style.opacity = '0';
        }
    });
});

// ===== 컬럼 정렬 기능 =====
document.querySelectorAll('.stat-header').forEach(header => {
    header.addEventListener('click', () => {
        const table = document.getElementById('statsTable');
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));
        const stat = header.getAttribute('data-stat');
        const isAsc = header.classList.toggle('asc');

        rows.sort((a, b) => {
            const aVal = a.querySelector(`[data-stat="${stat}"]`)?.getAttribute('data-value') || '';
            const bVal = b.querySelector(`[data-stat="${stat}"]`)?.getAttribute('data-value') || '';

            // 숫자 비교
            if (!isNaN(parseFloat(aVal)) && !isNaN(parseFloat(bVal))) {
                return isAsc ? aVal - bVal : bVal - aVal;
            }
            // 문자열 비교
            return isAsc ? aVal.localeCompare(bVal, 'ko') : bVal.localeCompare(aVal, 'ko');
        });

        rows.forEach(r => tbody.appendChild(r));
    });
});

// ===== 슬라이드(뷰) 전환 =====
let currentView = 1;
const totalViews = 2;

function updateView() {
    document.querySelectorAll(`[class*="group-"]`).forEach(col => {
        const groupNum = col.classList.contains('group-1') ? 1 : 2;
        col.style.display = (groupNum === currentView) ? '' : 'none';
    });
    document.querySelectorAll('.dot').forEach(dot => {
        dot.classList.toggle('active', parseInt(dot.dataset.view) === currentView);
    });
}

document.getElementById('prevView')?.addEventListener('click', () => {
    currentView = (currentView - 1 < 1) ? totalViews : currentView - 1;
    updateView();
});
document.getElementById('nextView')?.addEventListener('click', () => {
    currentView = (currentView % totalViews) + 1;
    updateView();
});
document.querySelectorAll('.dot').forEach(dot => {
    dot.addEventListener('click', () => {
        currentView = parseInt(dot.dataset.view);
        updateView();
    });
});

// 초기 뷰 설정
updateView();
