//
var target = document.getElementById('velocity');
var targetText = document.getElementById("velocityTextH1");
//
var opts = {
  lines: 12,
  angle: 0.00,
  lineWidth: 0.44,
  radiusScale: 0.80,
  pointer: {
    length: 0.5,
    strokeWidth: 0.0155,
    color: 'black'
  },
  limitMax: true,
  colorStart: '#00efff',
  generateGradient: false
};
var gauge = new Gauge(target).setOptions(opts);
gauge.maxValue = 100;
gauge.animationSpeed = 10;

function performSpeed(value){
  targetText.innerHTML = value;
  gauge.set(value);
}


