import * as moment from 'moment-timezone';
export enum DurationType {
    TWENTY_FOUR_HOURS="TWENTY_FOUR_HOURS", 
    FORTY_EIGHT_HOURS="FORTY_EIGHT_HOURS", 
    ONE_WEEK="ONE_WEEK"
}
export default class DateUtils {

    private static readonly hourDateFormat = "YYYY-MM-DDThh A";
    private static readonly dateFormat = "YYYY-MM-DD";
    private static readonly fullDateFormat = "YYYY-MM-DD HH:mm:ss";
    private static readonly fullMonthDate = "DD MMM, YYYY hh A";
    private static readonly timeZone = "+00:00";

    public static getLables(duration: DurationType) {
        const lables = [];
        if (DurationType.ONE_WEEK == duration) {
            let length = 6;
            for (let i=length;i>=0;i--) {
                lables[length-i]=this.getCurrentTimeAtTimeZone().subtract(i, "days").format(this.dateFormat);
            }
        } else {
            let length = 23;
            if (DurationType.FORTY_EIGHT_HOURS == duration) {
                length = 47;
            }
            for (let i=length;i>=0;i--) {
                lables[length-i]=this.getCurrentTimeAtTimeZone().subtract(i, "hours").format(this.hourDateFormat);
            }
        }
        return lables;
    }
    static days = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
    
    public static getCurrentTimeZoneLables(duration: DurationType) {
        const lables = {};
        if (DurationType.ONE_WEEK == duration) {
            let length = 6;
            for (let i=length;i>=0;i--) {
                const key=this.getCurrentTimeAtTimeZone().subtract(i, "days").format(this.dateFormat);
                const value = {
                    short: moment().subtract(i, "days").format(this.hourDateFormat),
                    long: moment().subtract(i, "days").format(this.fullMonthDate),
                    day: DateUtils.days[moment().subtract(i, "days").weekday()],
                };
                lables[key]=value;
            }
        } else {
            let length = 23;
            if (DurationType.FORTY_EIGHT_HOURS == duration) {
                length = 47;
            }
            for (let i=length;i>=0;i--) {
                const key=this.getCurrentTimeAtTimeZone().subtract(i, "hours").format(this.hourDateFormat);
                const value = {
                    short: moment().subtract(i, "hours").format(this.hourDateFormat),
                    long: moment().subtract(i, "hours").format(this.fullMonthDate),
                    day: DateUtils.days[moment().subtract(i, "hours").weekday()],
                };
                lables[key]=value;
            }
        }
        return lables;
    }

    public static getCurrentTimeAtTimeZone() {
        var time = moment();
        time.utcOffset(this.timeZone);
        return time;
    }
}