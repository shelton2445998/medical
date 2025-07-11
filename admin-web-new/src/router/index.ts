// @ts-ignore
import type {App} from 'vue';
import {createRouter, createWebHashHistory} from "vue-router";
import basicRoutes from './basic'

export const router = createRouter({
    history: createWebHashHistory(),
    routes: [...basicRoutes],
    strict: true,
});
