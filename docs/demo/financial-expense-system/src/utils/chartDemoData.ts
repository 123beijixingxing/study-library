class ParamsData {
  // 图表通用配置参数，更多详情参考echarts官网
  createChartConfig(object: any) {
    // const { grid, color, handMax, handMaxxValue, fiberValidStartPos, fiberValidEndPos } = object
    const { color } = object
    const chartOptionTemp = {
      // grid: { x: 80, x2: 90, y: 10, y2: 60 },
      grid3D: {
        // width: '100%',
        // height: '100%',
        boxWidth: 100,
        boxHeight: 100,
        boxDepth: 100,
        top: -100,
        bottom: 0,
        viewControl: {
          beta: 70,
          rotateSensitivity: 0,
          zoomSensitivity: 0,
          panSensitivity: 0,
          // distance: 180
        },
        light: {
          main: {
            shadow: false,
            quality: 'ultra',
            intensity: 1,
          },
        },
      },
      tooltip: {},
      xAxis3D: {
        id: 'xAxis3D1',
        type: 'value',
        nameTextStyle: {
          fontFamily: 'PingFangSC-Regular',
          fontSize: '12',
          padding: [0, 30, -10, 0],
        },
        min: 0,
        max: 100,
        splitNumber: 5,
        // interval: 10,
        // minInterval: 0.01,
        splitLine: {
          show: true,
        },
      },
      yAxis3D: {
        id: 'yAxis3D1',
        type: 'value',
        nameTextStyle: {
          fontFamily: 'PingFangSC-Regular',
          fontSize: '12',
          padding: [0, 30, -10, 0],
        },
        min: 0,
        max: 500,
        splitNumber: 5,
        // interval: 10,
        // minInterval: 0.01,
        splitLine: {
          show: true,
        },
        axisLabel: {
          // formatter: (value: any, index: any) => {
          formatter: (value: any) => {
            // const avg = parseInt(this.chartOption.series[0].data.length / 20);
            // return ((index + 1) % avg) - 1 === 0 ? value : '';
            return `${value}°`
          },
        },
      },
      zAxis3D: {
        id: 'zAxis3D1',
        type: 'value',
        name: 'dBmV',
        nameLocation: 'end',
        nameTextStyle: {
          fontFamily: 'PingFangSC-Regular',
          fontSize: '12',
          padding: [0, 30, -10, 0],
        },
        min: 0,
        max: 105,
        splitNumber: 5,
        minInterval: 0.01,
        splitLine: {
          show: true,
        },
      },
      // visualMap: {
      //   min: 0,
      //   max: 100,
      //   dimension: '电流电压'
      // },
      // dataset: {
      //   dimensions: ['位置', '温度', '电流电压'],
      //   source: []
      // },
      legend: {
        show: false,
        selected: {},
      },
      series: [
        {
          id: 'baseLine1',
          type: 'bar3D',
          // symbolSize: symbolSize,
          // grid3DIndex: 0,
          shading: 'lambert', // color,lambert,realistic
          // encode: {
          //   x: '位置',
          //   y: '温度',
          //   z: '电流电压',
          //   tooltip: [0, 1, 2]
          // },
          data: [],
          // label: {
          //   formatter: (param) => {
          //     return param.value[2].toFixed(1);
          //   }
          // },
          itemStyle: {
            opacity: 0.8,
            // borderWidth: 2,
            // color: {
            //   type: 'linear',
            //   x: 0,
            //   y: 0,
            //   x2: 0,
            //   y2: 1,
            //   colorStops: [
            //     {
            //       offset: 0,
            //       color: 'red' // 0% 处的颜色
            //     },
            //     {
            //       offset: 1,
            //       color: 'blue' // 100% 处的颜色
            //     }
            //   ],
            //   global: false // 缺省为 false
            // }
            // borderColor: '#FA6400',
            color: color ?? '#FA6400 ',
            // color: (params) => {
            //   // 定义颜色的映射
            //   const colors = ['#c23531', '#2f4554', '#61a0a8'];
            //   return colors[params.dataIndex % colors.length];
            // }
          },
          // emphasis: {
          //   label: {
          //     fontSize: 20,
          //     color: '#900'
          //   },
          //   itemStyle: {
          //     color: '#900'
          //   }
          // },
          animation: false,
          animationDuration: 100, //这里两个动画设置可以让图表更顺滑
          animationEasing: 'cubicInOut', //这里两个动画设置可以让图表更顺滑
        },
        {
          id: 'baseLine2',
          type: 'bar3D',
          // symbolSize: symbolSize,
          shading: 'lambert',
          // encode: {
          //   x: '位置',
          //   y: '温度',
          //   z: '电流电压',
          //   tooltip: [0, 1, 2]
          // },
          data: [],
          // label: {
          //   formatter: (param) => {
          //     return param.value[2].toFixed(1);
          //   }
          // },
          itemStyle: {
            opacity: 1,
            borderWidth: 2,
            // borderColor: '#61a0a8',
            color: '#61a0a8',
          },
          emphasis: {
            label: {
              fontSize: 20,
              color: '#900',
            },
            itemStyle: {
              color: '#900',
            },
          },
          animation: false,
          animationDuration: 0, //这里两个动画设置可以让图表更顺滑
          animationEasing: 'cubicInOut', //这里两个动画设置可以让图表更顺滑
        },
        {
          id: 'baseLine3',
          type: 'bar3D',
          // symbolSize: symbolSize,
          shading: 'lambert',
          // encode: {
          //   x: '位置',
          //   y: '温度',
          //   z: '电流电压',
          //   tooltip: [0, 1, 2]
          // },
          data: [],
          // label: {
          //   formatter: (param) => {
          //     return param.value[2].toFixed(1);
          //   }
          // },
          itemStyle: {
            opacity: 1,
            borderWidth: 2,
            // borderColor: '#0091DA ',
            color: '#0091DA ',
          },
          emphasis: {
            label: {
              fontSize: 20,
              color: '#0091DA ',
            },
            itemStyle: {
              color: '#0091DA ',
            },
          },
          animation: false,
          animationDuration: 0, //这里两个动画设置可以让图表更顺滑
          animationEasing: 'cubicInOut', //这里两个动画设置可以让图表更顺滑
        },
      ],
    }
    return chartOptionTemp
  }
}
export default new ParamsData()
