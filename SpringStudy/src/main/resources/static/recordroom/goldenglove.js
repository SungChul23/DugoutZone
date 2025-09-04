// 진입 애니메이션 & 은은한 스파클
document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.card-face');
    const observer = new IntersectionObserver((entries) => {
        entries.forEach((e) => {
            if (e.isIntersecting) {
                e.target.animate(
                    [
                        { transform: 'translateY(8px) rotateX(10deg)', opacity: 0 },
                        { transform: 'translateY(0) rotateX(6deg)', opacity: 1 }
                    ],
                    { duration: 600, easing: 'cubic-bezier(.2,.8,.2,1)', fill: 'forwards' }
                );
                observer.unobserve(e.target);
            }
        });
    }, { threshold: 0.2 });

    cards.forEach(c => observer.observe(c));

    // 수상자 카드에 금빛 파티클
    const winners = document.querySelectorAll('.card-face.winner');
    winners.forEach(w => sparkle(w));
});

function sparkle(target) {
    // CSS만으로 아주 가벼운 스파클을 만들기 위한 pseudo particle
    const n = 10;
    for (let i = 0; i < n; i++) {
        const dot = document.createElement('i');
        dot.style.position = 'absolute';
        dot.style.width = '6px';
        dot.style.height = '6px';
        dot.style.borderRadius = '50%';
        dot.style.pointerEvents = 'none';
        dot.style.background = 'radial-gradient(#fff, #ffd54a 60%, transparent 70%)';
        dot.style.left = (Math.random() * 100) + '%';
        dot.style.top = (Math.random() * 100) + '%';
        dot.style.opacity = '0';
        target.appendChild(dot);

        const delay = 300 + Math.random() * 1200;
        setTimeout(() => {
            dot.animate(
                [
                    { transform: 'translateY(0) scale(.6)', opacity: 0 },
                    { transform: 'translateY(-10px) scale(1)', opacity: .9, offset: .5 },
                    { transform: 'translateY(-20px) scale(.4)', opacity: 0 }
                ],
                { duration: 1400 + Math.random() * 600, iterations: Infinity, delay: Math.random()*900 }
            );
        }, delay);
    }
}
document.addEventListener("DOMContentLoaded", () => {
    const acc = document.querySelectorAll(".accordion");
    acc.forEach(btn => {
        btn.addEventListener("click", function () {
            this.classList.toggle("active");
            const panel = this.nextElementSibling;
            panel.style.display = panel.style.display === "block" ? "none" : "block";
        });
    });
});

