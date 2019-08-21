import Index from './components/index/indexPage'
import Dash from './components/dash/dashPage'
import Destinations from './components/destinations/destinationsPage'
import Objectives from './components/objectives/objectivePage'
import Profiles from './components/profiles/profilesPage'
import Trips from './components/trips/tripsPage'
import AdminPanel from './components/admin/adminPanel'
import Quests from './components/quests/questPage'

import VueRouter from 'vue-router';

const routes = [
    {
        path:"/",
        name: "index",
        component: Index,
        meta: {
            title: "Welcome to TravelEA"
        }
    },
    {
        path:"/dash",
        name: "dash",
        component: Dash,
        meta: {
            title: "Your Profile - TravelEA"
        }
    },
    {
        path:"/destinations",
        name: "destinations",
        component: Destinations,
        meta: {
            title: "Destinations - TravelEA"
        }
    },
    {
        path:"/objectives",
        name: "objectives",
        component: Objectives,
        meta: {
            title: "Objectives - TravelEA"
        }
    },
    {
        path:"/quests",
        name: "quests",
        component: Quests,
        meta: {
            title: "Quests - TravelEA"
        }
    },
    {
        path:"/profiles",
        name: "profiles",
        component: Profiles,
        meta: {
            title: "Profiles - TravelEA"
        }
    },
    {
        path:"/trips",
        name: "trips",
        component: Trips,
        meta: {
            title: "Trips - TravelEA"
        }
    },
    {
        path:"/admin",
        name: "admin",
        component: AdminPanel,
        meta: {
            title: "Administrator - TravelEA"
        }
    }
];

const router = new VueRouter({
    routes: routes,
    mode: 'history'
});

const defaultTitle = 'TravelEA';
router.afterEach((to, from) => {
    document.title = to.meta.title || defaultTitle;
});

export default router;

