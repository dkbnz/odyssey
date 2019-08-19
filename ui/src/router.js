import Index from './components/index/indexPage'
import Dash from './components/dash/dashPage'
import Destinations from './components/destinations/destinationsPage'
import Objectives from './components/objectives/objectivePage'
import Profiles from './components/profiles/profilesPage'
import Trips from './components/trips/tripsPage'
import AdminPanel from './components/admin/adminPanel'
import SingleProfile from './components/admin/singleProfile'
import Quests from './components/quests/questPage'

import VueRouter from 'vue-router';

const routes = [
    {
        path:"/",
        name: "index",
        component: Index
    },
    {
        path:"/dash",
        name: "dash",
        component: Dash
    },
    {
        path:"/destinations",
        name: "destinations",
        component: Destinations
    },
    {
        path:"/objectives",
        name: "objectives",
        component: Objectives
    },
    {
        path:"/quests",
        name: "quests",
        component: Quests
    },
    {
        path:"/profiles",
        name: "profiles",
        component: Profiles
    },
    {
        path:"/trips",
        name: "trips",
        component: Trips
    },
    {
        path:"/admin",
        name: "admin",
        component: AdminPanel
    },
    {
        path:"/singleProfile",
        name: "singleProfile",
        component: SingleProfile
    }
];

const router = new VueRouter({
    routes: routes,
    mode: 'history'
});

export default router;

