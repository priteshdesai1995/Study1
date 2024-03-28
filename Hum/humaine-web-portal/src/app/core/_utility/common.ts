import { Label } from 'ng2-charts';
import * as _ from 'lodash';
import { IResponse } from '../_model/response';
import { ToasterService } from './notify.service';
import { CONFIGCONSTANTS, graphAPIparam } from '../_constant/app-constant';
import DateUtils from './date';

export const preventEvent = ($event) => {
    $event.preventDefault();
    $event.stopPropagation();
    return false;
};

export const isEmpty = (value) => {
    return _.isEmpty(value) ? (_.isNumber(value) || _.isBoolean(value) ? false : true) : false;
};

export const getValueByKey = (obj, key, defaultVal: any = '') => {
    return _.get(obj, key, defaultVal);
};

export const successHandler = (toastr: ToasterService, data: IResponse<any>, successMessage: string, callBack?: (isSuccess: boolean) => void) => {
    if (data.statusCode === 200) {
        if (successMessage) {
            toastr.successMsg(successMessage);
        }
        callBack(true);
    } else {
        handleError(data, toastr, () => {
            callBack(false);
        })
    }
};

export const errorHandler = (toastr: ToasterService, data: IResponse<any>, callBack?: () => void) => {
    handleError(data, toastr, () => {
        callBack();
    })
};

export const getColumnValue = (data: any) => {
    if (data && data === CONFIGCONSTANTS.emptyValue) {
        return '';
    }
    return data;
}

const handleError = (data: IResponse<any>, toastr: ToasterService, callBack?: CallableFunction) => {
    if (!data) {
        callBack();
        return;
    }
    if (data.statusCode === 401) {
        callBack();
        return;
    }
    if (!data.errorList || data.errorList.length == 0) {
        callBack();
        return;
    }
    data.errorList.forEach(e => {
        toastr.errorMsg(e.message);
    });
    callBack();

}
export const formatNumber = (num: number, digits: number): string => {
    const si = [
        { value: 1, symbol: "" },
        { value: 1E3, symbol: "K" },
        { value: 1E6, symbol: "M" },
        { value: 1E12, symbol: "T" },
        { value: 1E15, symbol: "P" },
        { value: 1E18, symbol: "E" }
      ];
      const rx = /\.0+$|(\.[0-9]*[1-9])0+$/;
      let i;
      for (i = si.length - 1; i > 0; i--) {
        if (num >= si[i].value) {
          break;
        }
      }
      return (num / si[i].value).toFixed(digits).replace(rx, '$1') + si[i].symbol;
}

export enum DurationType {
    TWENTY_FOUR_HOURS="TWENTY_FOUR_HOURS", 
    FORTY_EIGHT_HOURS="FORTY_EIGHT_HOURS", 
    ONE_WEEK="ONE_WEEK"
}

export const getData = (duration: DurationType, data: any, defaultValue: any) => {
    const lables = getLabels(duration);
    if (!data) {
        return [];
    }
    const result = [];
    if (duration == DurationType.TWENTY_FOUR_HOURS) {
        lables.forEach(e => {
            if (Object.keys(data).includes(e)){
                result.push(data[e]);
            } else {
                result.push(defaultValue);
            }
        });
    } else if (duration == DurationType.FORTY_EIGHT_HOURS) {
        let ind = 1;
        lables.forEach(e => {      
            if (ind<=24) {
                if (data.twentyFourHoursDate && _.size(data.data[data.twentyFourHoursDate]) > 0) {
                    const filterObj = data.data[data.twentyFourHoursDate].filter(h => h.key == e);
                    if (_.size(filterObj) > 0) {
                        result.push(filterObj[0].value);
                    } else {
                        result.push(defaultValue);
                    }
                } else {
                    result.push(defaultValue);
                }
            } else {
                if (data.fourtyEightHoursDate && _.size(data.data[data.fourtyEightHoursDate]) > 0) {
                    const filterObj = data.data[data.fourtyEightHoursDate].filter(h => h.key == e-24);
                    
                    if (_.size(filterObj) > 0) {
                        result.push(filterObj[0].value);
                    } else {
                        result.push(defaultValue);
                    }
                } else {
                    result.push(defaultValue);
                }
            }
            ind++;
        });
    } else  {
        
        lables.forEach(e => {
            if (Object.keys(data.data).includes(e)){
                result.push(data.data[e][0].value || defaultValue);
            } else {
                result.push(defaultValue);
            }
        });
    }
    return result;
}

export const getKeyBasedData = (duration: DurationType | graphAPIparam, label: string = '', data: any, defaultValue: any = 0, extraParams = {}) => {
    const lables = getLabels(duration);
    if (!data) {
        return [
            { data: [], label: label, borderWidth: 1.5, ...extraParams },
          ]
    }
    const result = [];
    let ind = 1;
    if (graphAPIparam.ONE_WEEK == duration) {
               
        lables.forEach(e => {
            if (Object.keys(data.data).includes(e)){
                result.push(data.data[e][0].value || defaultValue);
            } else {
                result.push(defaultValue);
            }
        });

    } else {
        lables.forEach(e => {      
            if (ind<=24) {
                if (data.twentyFourHoursDate && _.size(data.data[data.twentyFourHoursDate]) > 0) {
                    const filterObj = data.data[data.twentyFourHoursDate].filter(h => h.key == e);
                    if (_.size(filterObj) > 0) {
                        result.push(filterObj[0].value);
                    } else {
                        result.push(defaultValue);
                    }
                } else {
                    result.push(defaultValue);
                }
            } else {
                if (data.fourtyEightHoursDate && _.size(data.data[data.fourtyEightHoursDate]) > 0) {
                    const filterObj = data.data[data.fourtyEightHoursDate].filter(h => h.key == e-24);
                    
                    if (_.size(filterObj) > 0) {
                        result.push(filterObj[0].value);
                    } else {
                        result.push(defaultValue);
                    }
                } else {
                    result.push(defaultValue);
                }
            }
            ind++;
        });
    }
    return [
        { data: result, label: label, borderWidth: 1.5 , ...extraParams},
      ]
}

export const getLabels = (duration: DurationType | graphAPIparam) => {
    const values = [];
    if (DurationType.TWENTY_FOUR_HOURS === duration) {
        for(let i = 1;i<=24;i++) {
            values.push(formatDurationText(i));
        }
    } else if (DurationType.FORTY_EIGHT_HOURS === duration) {
        for(let i = 1;i<=48;i++) {
            values.push(formatDurationText(i));
        }
    } else  {
        return ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
    }
    return values;
}
export const formatDurationText = (num: number) => {
    return num.toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false
      });
}


export const getTwentyFourHoursLables = ()=> {
    return getLabels(DurationType.TWENTY_FOUR_HOURS);
}

export const getFortyEightHoursLables = ()=> {
    return getLabels(DurationType.FORTY_EIGHT_HOURS);
}

export const processTwentyFourHoursData = (data = null, label: String = 'A', keyBased = false) => {
    if (!data) {
      return [
        { data: [], label: label, borderWidth: 1.5 },
      ]
    }
    return [
      { data: getData(DurationType.TWENTY_FOUR_HOURS, data, 0), label: label, borderWidth: 1.5 },
    ]
  }


export const processFortyEightHoursData = (data = null, label: String = 'A') => {
    if (!data) {
      return [
        { data: [], label: label, borderWidth: 1.5 },
      ]
    }
    return [
      { data: getData(DurationType.FORTY_EIGHT_HOURS, data, 0), label: label, borderWidth: 1.5 },
    ]
}

export const processData = (data,duration:DurationType, label: string = '', defaultValue:number = 0, otherConfig = {}) => {
    if (!data) {
      return [
        { data: [], label: label, borderWidth: 1.5, ...otherConfig },
      ]
    }
    const result = [];
    const lables = DateUtils.getLables(duration);
    lables.forEach(e => {
      if (data[e]) {
        result.push(data[e]);
      } else {
        result.push(defaultValue);
      }
    });
    return [
      { data: result, label: label, borderWidth: 1.5, ...otherConfig },
    ]
}

export const processTimeData = (data,duration:DurationType, label: string = '', defaultValue:number = 0, otherConfig = {}) => {
    if (!data) {
      return [
        { data: [], label: label, borderWidth: 1.5, ...otherConfig },
      ]
    }
    const result = [];
    const lables = DateUtils.getLables(duration);
    lables.forEach(e => {
      if (data[e]) {
        result.push(parseFloat((data[e]/1000)+"").toFixed(1));
      } else {
        result.push(defaultValue);
      }
    });
    return [
      { data: result, label: label, borderWidth: 1.5, ...otherConfig},
    ]
}

export const secToTime = (totalSeconds:any) => {
    totalSeconds = Math.abs(totalSeconds);
    let seconds = (totalSeconds % 60);
    let minutes = (totalSeconds % 3600) / 60;
    let hours = (totalSeconds % 86400) / 3600;
    let days = (totalSeconds % (86400 * 30)) / 86400;
    if (days >= 1) {
        return days.toFixed(1) + " days";
    } else if (hours >= 1) {
        return hours.toFixed(1) + " hrs";
    } else if (minutes >= 1) {
        return minutes.toFixed(1) + " mins";
    } else if (totalSeconds > 0) {
        return seconds.toFixed(1) + " secs";
    } else {
        return "";
    }
  }

export const showLabelsInMin = (totalSeconds:any) =>{
    totalSeconds = Math.abs(totalSeconds);
    let seconds = (totalSeconds % 60);
    let minutes = (totalSeconds % 3600) / 60;
    let hours = (totalSeconds % 86400) / 3600;
    let days = (totalSeconds % (86400 * 30)) / 86400;
    if (days >= 1) {
        return days.toFixed(1);
    } else if (hours >= 1) {
        return hours.toFixed(1);
    } else if (minutes >= 1) {
        return minutes.toFixed(1);
    } else if (totalSeconds > 0) {
        return seconds.toFixed(1);
    } else {
        return "";
    }
}