import { Color } from 'ng2-charts';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as d3 from 'd3';

import * as _ from 'lodash';
import { copyFileSync } from 'fs';
@Component({
  selector: 'bubble-view',
  templateUrl: './bubble-view.component.html',
  styleUrls: ['./bubble-view.component.scss']
})
export class BubbleViewComponent implements OnInit {
  _data: any;
  processDataSet = {};
  bubble = null;
  isZoom : boolean = false;
  @Input() set data(value) {
    // if (this.bubble == null) {
    //   this.bubble = this.bubbleChart();
    // }
    if (value) {
      this._data = value;
      if (this.bubble != null) {
        this.bubble('#chart', this.processData(this._data))
      }
    }
  }

  constructor(private router: Router) {
  }

  ngOnInit() {
    if (this.bubble == null) {
      this.bubble = this.bubbleChart();
    }
  }
  ngAfterViewInit(): void {
  }

  createId = (val) => {
    if (!val) return "";
    return val.toLowerCase().split(' ').join('-')
  }
  processData(data) {
    const ids = {
      "s": "s",
      "n": "n",
      "m": "m",
      "lg": "lg",
      "xl": "xl"
    }
    const radius = {
      [ids.s]: 15,
      [ids.n]: 20,
      [ids.m]: 25,
      [ids.lg]: 30,
      [ids.xl]: 40,
    }
    const chartData = [];
    const colors = {
      0: '#f6585e',
      1: '#ffb100',
      2: '#00bac8',
      3: 'gray'
    }

    
    const bigFiveWiseConfig = {
      1: {
        r: 80,
        x: 430,
        y: 225
      },
      2: {
        r: 70,
        x: 315,
        y: 120
      },
      3: {
        r: 50,
        x: 450,
        y: 80
      },
      4: {
        r: 40,
        x: 450,
        y: 80
      },
      5: {
        r: 40,
        x: 450,
        y: 80
      }
    }
    
    const getCoordinates = (r, a, cx, cy, currentRadius) => {
      let x = cx + r * Math.cos(a)
      let y = cy + r * Math.sin(a)

      return {
        x: x + currentRadius,
        y: y + currentRadius
      }
    }
    _.orderBy(data, ['totalUserCount'], ['desc']).forEach((e, ind) => {
      this.processDataSet[this.createId(e.bigFive)] = {
        parent: true,
        id: this.createId(e.bigFive),
        groupid: this.createId(e.bigFive),
        key: e.bigFive,
        count: e.totalUserCount,
        color: '#9d54fa',
        paramId: e.bigFiveId,
        size: bigFiveWiseConfig[ind + 1]['r'],
        xval: bigFiveWiseConfig[ind + 1]['x'],
        yval: bigFiveWiseConfig[ind + 1]['y'],
        childs: []
      };

      chartData.push({
        parent: true,
        id: this.createId(e.bigFive),
        groupid: this.createId(e.bigFive),
        key: e.bigFive,
        count: e.totalUserCount,
        color: '#934ff5',
        paramId: e.bigFiveId,
        size: bigFiveWiseConfig[ind + 1]['r'],
        xval: bigFiveWiseConfig[ind + 1]['x'],
        yval: bigFiveWiseConfig[ind + 1]['y']
      });

      // const sub = _.chunk(_.orderBy(e.groups, ['noOfUser'], ['desc']), Math.ceil(_.size(e.groups) / _.size(Object.keys(ids))));
      const sub = this.splitArrayIntoChunksOfLen(_.orderBy(e.groups, ['noOfUser'], ['desc']), Object.keys(colors).length);
      for (let i = 0; i < _.size(sub); i++) {
        sub[i].forEach((s, indx) => {
          const coord = getCoordinates(bigFiveWiseConfig[ind + 1]['r'], (90 + (indx * 5)), bigFiveWiseConfig[ind + 1]['x'], bigFiveWiseConfig[ind + 1]['y'], radius[ids[Object.keys(ids).reverse()[i]]]);
          this.processDataSet[this.createId(e.bigFive)]['childs'].push({
            parent: false,
            id: this.createId(e.bigFive) + "-sub",
            groupid: this.createId(e.bigFive),
            key: s.groupId,
            count: s.noOfUser,
            paramId: s.groupId,
            color: colors[i%_.size(Object.keys(colors))],
            size: radius[ids[Object.keys(ids).reverse()[i]]],
            parentX: bigFiveWiseConfig[ind + 1]['x'],
            parentY: bigFiveWiseConfig[ind + 1]['y'],
            xval: coord['x'],
            yval: coord['y']
          });
        });
      }
    });
    Object.keys(this.processDataSet).forEach(subEle => {
      Array.prototype.push.apply(chartData, this.processDataSet[subEle]['childs']);
    });
    return chartData;
  }

  splitArrayIntoChunksOfLen(arr, len) {
    const remain = arr.length % len;
    var chunks = [], i = 0, n = arr.length;
    while (i < n) {
        chunks.push(arr.slice(i, i += len));
    }
    return chunks;
  }
  
  bubbleChart() {
    const width = 640;
    const height = 370;

    var pack = d3.pack().size([80,80]).padding(3);
    
    // location to centre the bubbles
    const centre = { x: width / 2, y: height / 2 };

    // strength to apply to the position forces
    const forceStrength = 0.03;

    // these will be set in createNodes and chart functions
    let svg = null;
    let bubbles = null;
    let labels = null;
    let users = null;
    let count = null;
    let nodes = [];

    // charge is dependent on size of the bubble, so bigger towards the middle
    const charge = (d) => {
      return Math.pow(d.radius, 2.0) * 0.01
    }

    // create a force simulation and add forces to it
    const simulation = d3.forceSimulation()
      .force('charge', d3.forceManyBody().strength(charge))
      .force('center', d3.forceCenter(centre.x, centre.y))
      .force('x', d3.forceX().strength(forceStrength).x(centre.x))
      .force('y', d3.forceY().strength(forceStrength).y(centre.y))
      .force('collision', d3.forceCollide().radius((d: any) => d.radius + 4));



    simulation.stop();

    function createNodes(rawData) {
      // use max size in the data as the max in the scale's domain
      // note we have to ensure that size is a number
      const maxSize = d3.max(rawData, d => (+(d as any).size));

      // size bubbles based on area
      const radiusScale = d3.scaleSqrt()
        .domain([0, maxSize])
        .range([0, 80])

      // use map() to convert raw data into node data
      const myNodes = rawData.map(d => ({
        ...d,
        radius: radiusScale(+d.size),
        size: +d.size,
        x: Math.random() * 900,
        y: Math.random() * 800
      }))

      return myNodes;
    }


    let chart = function chart(selector, rawData) {
      // convert raw data into nodes data
      nodes = createNodes(rawData);
      // create svg element inside provided selector

      svg = d3.select(selector)
        .attr("perserveAspectRatio", "xMinYMid")
        .attr("class", "bubble")
        .on("zoom", zoom)
      
      
    // Add X axis
    var x = d3.scaleLinear()
    .range([ -10, width*2 ])
    svg.append("g")
    .attr("transform", "translate(0," + height*4 + ")")
    .call(d3.axisBottom(x).tickSize(-height*4).ticks(20))
    .select(".domain").remove()

  // Add Y axis
  var y = d3.scaleLinear()
    .range([ height*4, -10])
    .nice()
  svg.append("g")
    .call(d3.axisLeft(y).tickSize(-width*4).ticks(40))
    .select(".domain").remove()
// Customization
svg.selectAll(".tick line").attr("stroke", "#EBEBEB")

      // bind nodes data to circle elements
      const elements = svg.selectAll('.bubble')
        .data(nodes, d => d.id)
        .enter()
        .append('g')
        .attr('id', (d) => {
          return d['id'];
        })
        .attr('display', (d) => {
          return d['parent'] == false ? 'none' : 'block';
        })
        .attr('circle-type', (d) => {
          return d['parent'] == false ? 'sub' : 'parent';
        }).attr('padding', 2)

      var zoom = d3.zoom()
        .scaleExtent([0.5, 8])
        .on('zoom',  (event) => {
          var tooltip = document.getElementById("tooltip")
          tooltip.innerHTML = ``;
          elements
            .attr('transform', event.transform);
        });

      svg.call(zoom);
      var chart = document.querySelector('.bubble'),
        aspect = chart.clientWidth / chart.clientHeight,
        container = chart.parentElement;
      window.addEventListener('resize', (event) => {
        var targetWidth = container.clientWidth;
        chart.setAttribute("width", targetWidth + "");
        // chart.setAttribute("height", Math.round(targetWidth / aspect)+"");
      });

      window.dispatchEvent(new Event('resize'));


      // filters go in defs element
      var defs = svg.append("defs");

      var radialGradient = defs.append("radialGradient")
        .attr("id", "radial-gradient")
        .attr("cx", "50%")    //The x-center of the gradient
        .attr("cy", "50%")    //The y-center of the gradient
        .attr("r", "50%");   //The radius of the gradient

      //Add colors to make the gradient appear like a Sun
      radialGradient.append("stop")
        .attr("offset", "0%")
        .attr("stop-color", "#9e55fa");
      radialGradient.append("stop")
        .attr("offset", "30%")
        .attr("stop-color", "#9e55fa");
      radialGradient.append("stop")
        .attr("offset", "70%")
        .attr("stop-color", "#9e55fa");
      radialGradient.append("stop")
        .attr("offset", "100%")
        .attr("stop-color", "#a02cf1");
      // create filter with id #drop-shadow
      // height=130% so that the shadow is not clipped
      var filter = defs.append("filter")
        .attr("id", "drop-shadow")

      filter.append("feGaussianBlur")
        .attr("in", "SourceAlpha")
        .attr("stdDeviation", 1)
        .attr("result", "blur");

      // translate output of Gaussian blur to the right and downwards with 2px
      // store result in offsetBlur
      filter.append("feOffset")
        .attr("in", "blur")
        .attr("dx", 2)
        .attr("dy", 2)
        .attr("result", "offsetBlur");


      var feMerge = filter.append("feMerge");

      feMerge.append("feMergeNode")
        .attr("in", "offsetBlur")
      feMerge.append("feMergeNode")
        .attr("in", "SourceGraphic");


      bubbles = elements
        .append('circle')
        .classed('bubble', true)
        .attr('r', d => d.radius)
        .attr('fill', d => d.color)
        .style('cursor', 'pointer')
        .attr("stroke-width", 2)
        .style("fill",
          (d) => {
            if (d['parent'] == true) return "url(#radial-gradient)";
            return '';
          }
        )




       
      // bubbles.on('click', showTooltip)
      svg.selectAll('g')       
        .on("click", (event, d) => {
          let parentTooltip = '<div style="padding:15px;color: black;font-size: 12px;box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;"><div id="viewPersona" style="margin-bottom:5px;cursor:pointer;" ><img style="padding-right:5px;height:15px;" src="/assets/icon/eye-line.svg">    <span>View Persona</span></div>  <div id="expand" style="cursor:pointer;">    <img style="padding-right:5px;height:15px;" src="/assets/svg/fullscreen-exit-line.svg">    <span>Expand</span></div></div>';
          let childTooltip = '<div style="padding:15px;color: black;font-size: 12px;box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;"><div id="viewPersona" style="margin-bottom:5px;cursor:pointer;" ><img style="padding-right:5px;height:15px;" src="/assets/icon/eye-line.svg">    <span>View Persona</span></div>';
          var mouse = d3.pointer(event, svg);
            var tooltip = document.getElementById("tooltip")
            tooltip.style.top = `${mouse[1]}px`;
            tooltip.style.left = `${mouse[0]}px`;
            const createId = (val) => {
              if (!val) return "";
              return val.toLowerCase().split(' ').join('-')
            }
            if (d['parent']) { 
              var dispVal = null;
              if (svg.select(`#${createId(d['id'])}-sub`)) {
                dispVal = svg.select(`#${createId(d['id'])}-sub`).style('display');
              }
              if (dispVal == 'block') {
                parentTooltip = '<div style="padding:15px;color: black;font-size: 12px;box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px;"><div id="viewPersona" style="margin-bottom:5px;cursor:pointer;" ><img style="padding-right:5px;height:15px;" src="/assets/icon/eye-line.svg">    <span>View Persona</span></div>  <div id="expand" style="cursor:pointer;">    <img style="padding-right:5px;height:15px;" src="/assets/svg/fullscreen-exit-line.svg">    <span>Shrink</span></div></div>';
              }
              tooltip.innerHTML = parentTooltip;
                d3.select("#expand")
                .on('click', () => {
                  const createId = (val) => {
                    if (!val) return "";
                    return val.toLowerCase().split(' ').join('-')
                  }
                  if (d['parent'] == true) {
                    svg.selectAll(`[circle-type="sub"]:not(#${createId(d['id'])}-sub)`).style('display', 'none');
                    var dispVal = null;
                    if (svg.select(`#${createId(d['id'])}-sub`)) {
                      dispVal = svg.select(`#${createId(d['id'])}-sub`).style('display');
                    }
                    svg.selectAll(`#${createId(d['id'])}-sub`).style('display', dispVal == 'none' ? 'block' : 'none');
                    if (dispVal == 'none' || dispVal == null) {
                      svg.selectAll(`[circle-type="parent"]`).style('opacity', .3);
                      svg.select(`#${createId(d['id'])}`).style('opacity', 1);
                    } else {
                      svg.selectAll(`[circle-type="parent"]`).style('opacity', 1);
                    }
                    tooltip.innerHTML =``;
                  }
                });
                d3.select("#viewPersona")
                .on('click', () => {
                  if (d['parent']) {
                    this.router.navigate(['/customer-journey/my-user-groups/groups/viewPersona/', d['paramId']])
                  }
                  else {
                    this.router.navigate(['/customer-journey/my-user-groups/viewPersona/', d['paramId']])
                  }
                });
            } else {  
              tooltip.innerHTML = childTooltip;
              d3.select("#viewPersona")
              .on('click', () => {
                if (d['parent']) {
                  this.router.navigate(['/customer-journey/my-user-groups/groups/viewPersona/', d['paramId']])
                }
                else {
                  this.router.navigate(['/customer-journey/my-user-groups/viewPersona/', d['paramId']])
                }
              });
            }
  
        })        
        

      labels = elements
        .append('text')
        .attr('dy', d => {
          if (d['parent'] == true) return '-1.5em';
          return '0 em'
        })
        .style('text-anchor', 'middle')
        .style('cursor', 'pointer')
        .style('font-size', (d) => {
          if (d['parent'] == true) return 10;
          //return 9;
          return 14;
        })
        .style('font-family', '"SofiaProMedium", sans-serif !important')
        .style('fill', 'white')
        .text(d => d.key)

      count = elements.append('text')
        .attr('dy', '0.5em')
        .style('text-anchor', 'middle')
        .style('cursor', 'pointer')
        .style('font-size', (d) => {
          if (d['parent'] == true) return 20;
          return 14;
        })
        .style('font-family', '"SofiaProMedium", sans-serif !important')
        .style('fill', 'white')
        .text(d => {
          return d.parent ? d.count : '';
        })

      users = elements.append('text')
        .attr('dy', (d) => {
          if (d['parent'] == true) return '2.3em';
          return '2.6em';
        })
        .style('text-anchor', 'middle')
        .style('cursor', 'pointer')

        .style('font-size', (d) => {
          if (d['parent'] == true) return 13;
          return 10;
        })
        .style('font-family', '"SofiaProMedium", sans-serif !important')
        .style('fill', 'white')
        .text(d => {
          return d.parent ? 'Users' : '';
        })

      // set simulation's nodes to our newly created nodes array
      // simulation starts running automatically once nodes are set
      simulation.nodes(nodes)
        .on('tick', ticked)
        .restart();
    }

    function ticked() {
      bubbles
        .attr('cx', d => {
          if (d['parent'] == true) {
            // return d.xval;
          }
          return d.x;
        })
        .attr('cy', d => {
          if (d['parent'] == true) {
            // return d.yval;

          }
          return d.y
        })

      labels
        .attr('x', d => {
          if (d['parent'] == true) {
            // return d.xval;
          }
          return d.x
        })
        .attr('y', d => {
          if (d['parent'] == true) {
            // return d.yval;
          }
          return d.y
        })

      users.attr('x', d => {
        if (d['parent'] == true) {
          // return d.xval;
        }
        return d.x
      })
        .attr('y', d => {
          if (d['parent'] == true) {
            // return d.yval;
          }
          return d.y
        })

      count.attr('x', d => {
        if (d['parent'] == true) {
          // return d.xval;
        }
        return d.x
      })
        .attr('y', d => {
          if (d['parent'] == true) {
            // return d.yval;
          }
          return d.y
        })
    }

    // return chart function from closure
    return chart;
  }

  redirectViewPersona(bigFiveId) {
    this.router.navigate(['/customer-journey/my-user-groups/groups/viewPersona/', bigFiveId])
  }

}
