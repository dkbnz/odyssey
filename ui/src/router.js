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
            title: "Welcome to TravelEA"
        }
    },
    {
        path:"/profile",
        name: "profile",
        component: ProfilePage,
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
        path:"/quests",
        name: "quests",
        component: Quests,
        meta: {
            title: "Quests - TravelEA"
        }
    },
    {
        path:"/leaderboard",
        name: "leaderboard",
        component: Leaderboard,
        meta: {
            title: "Leaderboard - TravelEA"
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
    },
    {
        path:"/credits",
        name: "credits",
        component: Credits,
        meta: {
            title: "Credits - TravelEA"
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

const defaultTitle = 'TravelEA';
router.afterEach((to, from) => {
    document.title = to.meta.title || defaultTitle;
});
export default router;

