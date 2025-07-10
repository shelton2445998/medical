/**
 * 日期时间格式化工具
 */

/**
 * 格式化日期为 YYYY-MM-DD
 * @param date 日期对象或日期字符串
 * @returns 格式化后的日期字符串
 */
export function formatDate(date: string | number | Date): string {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

/**
 * 格式化日期时间为 YYYY-MM-DD HH:mm:ss
 * @param date 日期对象或日期字符串
 * @returns 格式化后的日期时间字符串
 */
export function formatDateTime(date: string | number | Date): string {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

/**
 * 将日期对象转换为指定格式的字符串
 * @param date 日期对象或日期字符串
 * @param format 格式字符串，如 'YYYY-MM-DD HH:mm:ss'
 * @returns 格式化后的日期时间字符串
 */
export function formatDateByFormat(date: string | number | Date, format: string = 'YYYY-MM-DD'): string {
  if (!date) return '';
  const d = new Date(date);
  const year = d.getFullYear().toString();
  const month = (d.getMonth() + 1).toString().padStart(2, '0');
  const day = d.getDate().toString().padStart(2, '0');
  const hours = d.getHours().toString().padStart(2, '0');
  const minutes = d.getMinutes().toString().padStart(2, '0');
  const seconds = d.getSeconds().toString().padStart(2, '0');

  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds);
}

/**
 * 获取当前日期，格式为 YYYY-MM-DD
 * @returns 当前日期字符串
 */
export function getCurrentDate(): string {
  return formatDate(new Date());
}

/**
 * 获取当前日期时间，格式为 YYYY-MM-DD HH:mm:ss
 * @returns 当前日期时间字符串
 */
export function getCurrentDateTime(): string {
  return formatDateTime(new Date());
} 