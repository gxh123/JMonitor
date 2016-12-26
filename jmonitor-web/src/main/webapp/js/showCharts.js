/**
 * Created by gxh on 2016/12/19.
 */
var chart_CPU = echarts.init(document.getElementById('chart_CPU'));
chart_CPU.setOption(option = {
    title: {
        text: 'CPU使用率'
    },
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            return params[0].name + '<br />' + '使用率：'+params[0].value+'%';
        }
    },
    xAxis: {
        data: [],
        axisTick: {
            alignWithLabel: true
        },
        boundaryGap: false
    },
    yAxis: {
        axisLabel: {
            formatter: '{value} %'
        }
    },
    visualMap: {
        top: 10,
        right: 10,
        pieces: [{
            gt: 0,
            lte: 50,
            color: '#096'
        }, {
            gt: 50,
            lte: 80,
            color: '#ffde33'
        }, {
            gt: 80,
            lte: 100,
            color: '#cc0033'
        }],
        outOfRange: {
            color: '#999'
        }
    },
    series: {
        name: '百分比',
        type: 'line',
        data: [],
        markLine: {
            silent: true,
            data: [{
                yAxis: 50
            }, {
                yAxis: 80
            }]
        }
    }
});


var times_CPU = [];
var nums_CPU = [];
$.ajax({
    type: "post",
    async: true,            //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
    url: "/status/cpuRatio",
    dataType: "json",
    success: function (result) {
        if (result) {
            for (var i = 0; i < result.length; i = i + 1) {
                times_CPU.push(result[i].time);
            }
            for (var i = 0; i < result.length; i = i + 1) {
                nums_CPU.push(result[i].value);
            }
            chart_CPU.setOption({
                xAxis: {
                    data: times_CPU
                },
                series: [{
                    name: '百分比',
                    data: nums_CPU
                }]
            });
        }
    },
    error: function (errorMsg) {
        alert("图表请求数据失败!");
        chart_CPU.hideLoading();
    }
})


//----------------------------------------------------------------------------------------------------
var chart_disk = echarts.init(document.getElementById('chart_disk'));
chart_disk.setOption(option = {
    title: {
        text: '硬盘使用情况'
    },
    legend: {
        data: ['已使用', '未使用']
    },
    xAxis: [
        {
            type: 'category',
            data: []
        }
    ],
    yAxis: [
        {
            type: 'value',
            axisLabel: {
                formatter: '{value} G'
            }
        },

    ],
    series: [
        {
            name: '已使用',
            type: 'bar',
            stack: 'disk',
            data: [],
            itemStyle: {
                normal: {
                    color: '#A8A8A8',
                    label: {show: true}
                }
            }
        },
        {
            name: '未使用',
            type: 'bar',
            stack: 'disk',
            data: [],
            itemStyle: {
                normal: {
                    color: '#32CD32',
                    label: {show: true}
                }
            }
        },
    ]
});

var disk_names = [];
var disk_used = [];
var disk_free = [];
$.ajax({
    type: "post",
    async: true,
    url: "/status/disk",
    dataType: "json",
    success: function (result) {
        if (result) {
            for (var i = 0; i < result.names.length; i = i + 1) {
                disk_names.push(result.names[i]);
            }
            for (var i = 0; i < result.used.length; i = i + 1) {
                disk_used.push(result.used[i]);
            }
            for (var i = 0; i < result.unused.length; i = i + 1) {
                disk_free.push(result.unused[i]);
            }
            chart_disk.setOption({
                xAxis: {
                    data: disk_names
                },
                series: [{
                    name: '未使用',
                    data: disk_free
                },{
                    name: '已使用',
                    data: disk_used
                }]
            });
        }
    },
    error: function (errorMsg) {
        alert("图表请求数据失败!");
        chart_disk.hideLoading();
    }
})


//----------------------------------------------------------------------------------------------------
var chart_ram = echarts.init(document.getElementById('chart_ram'));
chart_ram.setOption(option = {
    title: {
        text: '虚拟机内存使用情况',
    },
    tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c}M ({d}%)"
    },
    color: ['#A8A8A8', '#32CD32'],
    legend: {
        orient: 'horizontal',
        left: 'center',
        data: ['已使用', '未使用']
    },
    series: [
        {
            name: '内存',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: [],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
});

var memory_data = [{value:0, name:'已使用'},
                   {value:0, name:'未使用'}];
$.ajax({
    type: "post",
    async: true,
    url: "/status/memory",
    dataType: "json",
    success: function (result) {
        if (result) {
            memory_data[0].value = result.used;
            memory_data[1].value= result.unused;
            chart_ram.setOption({
                series: [{
                        data: memory_data
                    }
                ]
            });
        }
    },
    error: function (errorMsg) {
        alert("图表请求数据失败!");
        chart_ram.hideLoading();
    }
})


//-----------------------------------------------------------------------------------------------------------------
var chart_heap = echarts.init(document.getElementById('chart_heap'));
chart_heap.setOption(option = {
    title: {
        text: '堆内存使用情况'
    },
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            return params[0].name + '<br />' + '堆内存：'+params[0].value+'M';
        }
    },
    xAxis: {
        data: [],
        axisTick: {
            alignWithLabel: true
        },
        boundaryGap: false,
    },
    yAxis: {
        axisLabel: {
            formatter: '{value} M'
        }
    },
    series: {
        name: '堆内存',
        type: 'line',
        data: [],
        markLine: {
            silent: true,
            data: [{
                yAxis: 150
            }]
        }
    }
});


var times_heap = [];
var nums_heap = [];
$.ajax({
    type: "post",
    async: true,
    url: "/status/heap",
    dataType: "json",
    success: function (result) {
        if (result) {
            for (var i = 0; i < result.length; i = i + 1) {
                times_heap.push(result[i].time);
            }
            for (var i = 0; i < result.length; i = i + 1) {
                nums_heap.push(result[i].value);
            }
            chart_heap.setOption({
                xAxis: {
                    data: times_heap
                },
                series: [{
                    name: '堆内存',
                    data: nums_heap
                }]
            });
        }
    },
    error: function (errorMsg) {
        alert("图表请求数据失败!");
        chart_heap.hideLoading();
    }
})

//-----------------------------------------------------------------------
var chart_thread = echarts.init(document.getElementById('chart_thread'));
chart_thread.setOption(option = {
    title: {
        text: '线程数'
    },
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            return params[0].name + '<br />' + '线程数：'+params[0].value;
        }
    },
    xAxis: {
        data: [],
        axisTick: {
            alignWithLabel: true
        },
        boundaryGap: false
    },
    yAxis: {},
    series: {
        name: '线程数',
        type: 'line',
        data: []
    }
});


var times_thread = [];
var nums_thread = [];
$.ajax({
    type: "post",
    async: true,
    url: "/status/thread",
    dataType: "json",
    success: function (result) {
        if (result) {
            for (var i = 0; i < result.length; i = i + 1) {
                times_thread.push(result[i].time);
            }
            for (var i = 0; i < result.length; i = i + 1) {
                nums_thread.push(result[i].value);
            }
            chart_thread.setOption({
                xAxis: {
                    data: times_thread
                },
                series: [{
                    name: '线程数',
                    data: nums_thread
                }]
            });
        }
    },
    error: function (errorMsg) {
        alert("图表请求数据失败!");
        chart_thread.hideLoading();
    }
})
