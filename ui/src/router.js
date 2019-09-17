import Index from './components/index/indexPage'
import ProfilePage from './components/dash/dashPage'
import Destinations from './components/destinations/destinationsPage'
import Leaderboard from './components/profiles/leaderboardPage'
import Trips from './components/trips/tripsPage'
import AdminPanel from './components/admin/adminPanel'
import Quests from './components/quests/questPage'
import Credits from './components/helperComponents/creditsPage'

import VueRouter from 'vue-router';


const routes = [
    {
        path:"/",
        name: "index",
        component: Index,
        meta: {
            title: "Welcome to Odyssey"
        }
    },
    {
        path:"/profile",
        name: "profile",
        component: ProfilePage,
        meta: {
            title: "Your Profile - Odyssey"
        }
    },
    {
        path:"/destinations",
        name: "destinations",
        component: Destinations,
        meta: {
            title: "Destinations - Odyssey"
        }
    },
    {
        path:"/quests",
        name: "quests",
        component: Quests,
        meta: {
            title: "Quests - Odyssey"
        }
    },
    {
        path:"/leaderboard",
        name: "leaderboard",
        component: Leaderboard,
        meta: {
            title: "Leaderboard - Odyssey"
        }
    },
    {
        path:"/trips",
        name: "trips",
        component: Trips,
        meta: {
            title: "Trips - Odyssey"
        }
    },
    {
        path:"/admin",
        name: "admin",
        component: AdminPanel,
        meta: {
            title: "Administrator - Odyssey"
        }
    },
    {
        path:"/credits",
        name: "credits",
        component: Credits,
        meta: {
            title: "Credits - Odyssey"
        }
    },
    {
        path: '*',
        redirect: '/profile'
    }
];

const router = new VueRouter({
    routes: routes,
    mode: 'history'
});

const defaultTitle = 'Odyssey';
router.afterEach((to, from) => {
    document.title = to.meta.title || defaultTitle;
});
export default router;

