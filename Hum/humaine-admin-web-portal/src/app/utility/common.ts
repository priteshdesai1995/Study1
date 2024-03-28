import * as _ from 'lodash';
import { ToasterService } from './notify.service';
import { CONFIGCONSTANTS } from '../_constant/app.constant';
import { IResponse } from '../_model/response';

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


