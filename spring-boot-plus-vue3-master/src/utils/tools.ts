import {computeDateType} from "@/enum";
import {dateFormat} from "@/utils/index";

/**
 * 时间加减
 * */
export function computeDate(num:number, type:computeDateType, timestamp:number|string|Date=new Date()):Date|string{
    const date = new Date(timestamp)
    switch (type) {
        case computeDateType.year:
            return dateFormat(date.setFullYear(date.getFullYear()+num));
        case computeDateType.month:
            return dateFormat(date.setMonth(date.getMonth()+num));
        case computeDateType.day:
            return dateFormat(date.setDate(date.getDate()+num));
        case computeDateType.hour:
            return dateFormat(date.setHours(date.getHours()+num));
        case computeDateType.minute:
            return dateFormat(date.setMinutes(date.getMinutes()+num));
        case computeDateType.second:
            return dateFormat(date.setSeconds(date.getSeconds()+num));
    }
} 