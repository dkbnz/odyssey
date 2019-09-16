<template>
    <div>
        <navbar-main :profile="profile"></navbar-main>
        <div class="bg-white m-2 pt-3 pl-3 pr-3 pb-3 rounded-lg">
            <h1 class="page-title">Credits</h1>
            <p class="page-title"><i>We have a few external sources that have made this website possible</i></p>
            <b-row>
                <b-col>
                    <h2>Traveller Types</h2>
                    <b-list-group>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Backpacker Image</h4>
                                    <a href="https://unsplash.com/photos/gbaeHydpgtE" target="_blank">Source Link</a><br/>
                                    <small>https://unsplash.com/photos/gbaeHydpgtE</small><br/>
                                    <small>Author: <a href="https://unsplash.com/@chris_hayashi" target="_blank">Chris Hayashi</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['backpacker']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Functional/Business Image</h4>
                                    <a href="https://unsplash.com/photos/ZGjbiukp_-A" target="_blank">Source Link</a><br/>
                                    <small>https://unsplash.com/photos/ZGjbiukp_-A</small><br/>
                                    <small>Author: <a href="https://unsplash.com/@samsonyyc" target="_blank">Samson Creative</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['business']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Frequent Weekender Image</h4>
                                    <a href="https://unsplash.com/photos/BPCsppbNRMI" target="_blank">Source Link</a><br/>
                                    <small>https://unsplash.com/photos/BPCsppbNRMI</small><br/>
                                    <small>Author: <a href="https://unsplash.com/@everettm" target="_blank">Everett McIntire</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['frequentWeekender']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Gap Year Image</h4>
                                    <a href="https://unsplash.com/photos/n5LNMjr7gyI" target="_blank">Source Link</a><br/>
                                    <small>https://unsplash.com/photos/n5LNMjr7gyI</small><br/>
                                    <small>Author: <a href="https://unsplash.com/@courtmarie" target="_blank">Court Prather</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['gapYear']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Groupie Image</h4>
                                    <a href="https://unsplash.com/photos/zdSoe8za6Hs" target="_blank">Source Link</a><br/>
                                    <small>https://unsplash.com/photos/zdSoe8za6Hs</small><br/>
                                    <small>Author: <a href="https://unsplash.com/@homeschool" target="_blank">Levi Guzman</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['groupie']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Holiday Maker Image</h4>
                                    <a href="https://unsplash.com/photos/Yb-n0JHlsWY" target="_blank">Source Link</a><br/>
                                    <small>https://unsplash.com/photos/Yb-n0JHlsWY</small><br/>
                                    <small>Author: <a href="https://unsplash.com/@trapnation" target="_blank">Andre Benz</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['holidayMaker']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Thrillseeker Image</h4>
                                    <a href="https://unsplash.com/photos/FZ0qzjVF_-c" target="_blank">Source Link</a><br/>
                                    <small>https://unsplash.com/photos/FZ0qzjVF_-c</small><br/>
                                    <small>Author: <a href="https://unsplash.com/@huchenme" target="_blank">Chen Hu</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['thrillseeker']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                        <h2>Welcome Page</h2>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col>
                                    <h4>Welcome Page Image</h4>
                                    <small>Author: Isaac Worsley</small><br/>
                                    <small>Member of team Everyware</small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['welcomePage']" width="190%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                    </b-list-group>
                </b-col>
                <b-col>
                    <h2>Badges</h2>
                    <b-list-group>
                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Planner Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/direction_1191593#term=signpost&page=1&position=3" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/direction_1191593#term=signpost&page=1&position=3</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['planner']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Cartographer Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/address_1076323#term=location&page=1&position=5" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/address_1076323#term=location&page=1&position=5</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['cartographer']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Writer Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/map_1606528#term=map%20scroll&page=1&position=3" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/map_1606528#term=map%20scroll&page=1&position=3</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['writer']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Solver Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/loupe_751381#term=search&page=1&position=4" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/loupe_751381#term=search&page=1&position=4</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['solver']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Explorer Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/search_1150637#term=search%20map&page=1&position=16" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/search_1150637#term=search%20map&page=1&position=16</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['explorer']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Adventurer Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/helmet_1455027#term=greece&page=1&position=8" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/helmet_1455027#term=greece&page=1&position=8</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['adventurer']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Wayfarer Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/hiking_1706736#term=walking&page=1&position=4" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/hiking_1706736#term=walking&page=1&position=4</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['wayfarer']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Streaker Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/flash_656403#term=lightning%20bolt&page=1&position=21" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/flash_656403#term=lightning%20bolt&page=1&position=21</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['streaker']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Overachiever Badge</h4>
                                    <a href="https://www.flaticon.com/free-icon/laurel_876862#term=award&page=1&position=22" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/laurel_876862#term=award&page=1&position=22</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['overachiever']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>

                        <b-list-group-item class="flex-column justify-content-center">
                            <b-row>
                                <b-col cols="9">
                                    <h4>Star Symbol</h4>
                                    <a href="https://www.flaticon.com/free-icon/favorite_1721927" target="_blank">Source Link</a><br/>
                                    <small>https://www.flaticon.com/free-icon/favorite_1721927</small><br/>
                                    <small>Author: <a href="https://www.freepik.com" target="_blank">Freepik</a></small>
                                </b-col>
                                <b-col>
                                    <b-img :src="assets['star']" width="100%"></b-img>
                                </b-col>
                            </b-row>
                        </b-list-group-item>
                    </b-list-group>
                </b-col>
            </b-row>
        </div>
        <footer-main></footer-main>
    </div>
</template>

<script>
    import NavbarMain from "./navbarMain";
    import FooterMain from "./footerMain";
    export default {
        name: "creditsPage",

        props: {
            profile: Object
        },

        components: {
            FooterMain,
            NavbarMain
        }
    }
</script>
<style>
    @import "../../css/credits.css";
</style>