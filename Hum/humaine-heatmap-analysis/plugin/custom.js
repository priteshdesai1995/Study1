
// const gradientCfg = { // heatmap gradient color range
//     '0.15': '#6ad180', // green
//     '0.25': '#7cd573',
//     '0.35': '#90d865',
//     '0.45': '#a4da57',
//     '0.55': '#badc48',
//     '0.65': '#c9cf35',
//     '0.75': '#d6c226',
//     '0.80': '#e2b41c',
//     '0.85': '#e2961d',
//     '0.90': '#dd7826',
//     '0.95': '#d25c30',
//     '1.0': '#c24039' // highest red
// };

const gradientCfg = { // heatmap gradient color range
    '1.0': '#2EE1F0' // highest red
};
const colorCombination = {
    1: "#CE15FB",
    2: "#7033FF",
    3: "#2EE1F0"
}

function chunk(arr, start, amount) {
    var result = [],
        i,
        start = start || 0,
        amount = amount || 500,
        len = arr.length;

    do {
        //console.log('appending ', start, '-', start + amount, 'of ', len, '.');
        result.push(arr.slice(start, start + amount));
        start += amount;

    } while (start < len);

    return result;
};

function splitArrayIntoChunksOfLen(arr) {
    const len = Object.keys(colorCombination).length;
    const remain = arr.length % len;
    var chunks = [], i = 0, n = arr.length;
    while (i < n) {
        chunks.push(arr.slice(i, i += len));
    }
    return chunks;
}
function getFinalElement(el) {
    const childElements = el.childNodes;
    const isImgInChild = [...childElements].map(e => e.tagName).includes("IMG");
    if (isImgInChild) {
        const indx = [...childElements].map(e => e.tagName).indexOf("IMG");
        if (indx > -1) {
            return childElements[indx];
        }
    }
    return el;
}
function getFinalElementIs(el, elementName = "IMG", callback = null) {
    const childElements = el.childNodes;
    const isImgInChild = [...childElements].map(e => e.tagName).includes(elementName);
    if (isImgInChild) {
        const indx = [...childElements].map(e => e.tagName).indexOf("IMG");
        if (indx > -1) {
            if (callback) {
                callback(childElements, indx);
            }
            return true;
        }
    }
    return false;
}
function generateHeatMap(elementsValueMap) {
    const len = Object.keys(colorCombination).length;
    const valuesArray = [...new Set(elementsValueMap.map(e => e.value))].sort((e1, e2) => e2 - e1);
    const subArrays = chunk(valuesArray, 0, Math.floor(valuesArray.length / len));
    const valueViseCode = {};
    subArrays.forEach((e, ind) => {
        e.forEach(sa => {
            valueViseCode[sa] = colorCombination[ind + 1];
        });
    });
    elementsValueMap.forEach(e => {
        e['color'] = valueViseCode[e.value];
    })
    const backDrop = document.querySelector(".hum-backdrop");
    if (backDrop) {
        backDrop.style.height = document.documentElement.scrollHeight + "px";
    }
    if (elementsValueMap.constructor === Array) {
        elementsValueMap.forEach((value, index) => {
            if (value.element) {
                var eles = document.querySelectorAll(value.element);
                eles.forEach(ele => {
                    if (!checkElementIsInFooter(ele)) {
                        generateHetMapToElement(ele, value.value, index, value.count, value.color);
                    }
                });
            }
        });
    }

}

function generateHetMapToElement(ele, value, index, count, color) {
    if (!value) value = 0;
    if (ele) {
        const obj = {
            min: 0,
            maxOpacity: .5,
            minOpacity: 0,
            blur: .75,
            container: ele
        };
        obj['radius'] = Math.floor(ele.clientWidth / 2);
        ele.classList.add("hum-track-ele");
        // var heatmap = h337.create({
        //   ...obj
        // });


        let centerX = Math.floor(ele.clientWidth / 2);
        let centerY = Math.floor(ele.clientHeight / 2);

        // heatmap.setData({
        //     data: [{ x: centerX, y: centerY, value: value}]
        // });
        var newNode = document.createElement('div');
        newNode.className = 'humtooltip';
        newNode.innerHTML = `${count} clicks(${value}%)`;
        newNode.id = 'hum-tooltip-sc-' + index;
        newNode.style.backgroundColor = color;

        // if (ele.parentElement) {
        //     ;   
        // } else {
        //     document.getElementsByTagName("body")[0].appendChild(newNode);
        // }


        var fnalEles = getFinalElement(ele);
        if (fnalEles.tagName == 'IMG') {
            insertAfter(fnalEles, newNode);
        } else {
            ele.appendChild(newNode)
        }
        ele.style.display = "inline-block";
        // newNode.style.position = "absolute";
        const offset = getOffset(getFinalElement(ele));
        newNode.style.left = offset.x + 'px';
        newNode.style.top = offset.y + 'px';

        getFinalElement(ele).style.border = "dotted " + color;

        // createToolTip(ele, newNode);
    }
}
function getOffset(el) {
    var _x = 0;
    var _y = 0;
    while (el && !isNaN(el.offsetLeft) && !isNaN(el.offsetTop)) {
        _x += el.offsetLeft - el.scrollLeft;
        _y += el.offsetTop - el.scrollTop;
        el = el.offsetParent;
    }
    return { top: _y, left: _x };
}

function checkElementIsInFooter(element) {
    if (!element) return true;
    var footer = document.getElementsByTagName("footer")[0]
    if (!footer) {
        return false;
    }
    return footer.contains(element);
}

function createToolTip(button, tooltip) {
    Popper.createPopper(button, tooltip, {
        placement: 'auto',
    });
}

function insertAfter(referenceNode, newNode) {
    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}
