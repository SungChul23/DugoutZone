particlesJS("particles-js", {
    particles: {
        number: {
            value: 80,
            density: { enable: true, value_area: 800 }
        },
        color: { value: "#ffd700" },
        shape: { type: "circle" },
        opacity: { value: 0.4, random: true },
        size: { value: 3, random: true },
        line_linked: { enable: false },
        move: {
            enable: true,
            speed: 1.2,
            direction: "bottom",
            out_mode: "out"
        }
    },
    interactivity: {
        detect_on: "canvas",
        events: {
            onhover: { enable: false },
            onclick: { enable: false }
        }
    },
    retina_detect: true
});
