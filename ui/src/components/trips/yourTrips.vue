<template>
    <div class="container">
        <div id="upcomingTrips">
            <h1 class="page_title">Upcoming Trips</h1>
            <p class="page_title"><i>Here are your upcoming trips!</i></p>
            <b-table hover striped outlined
                     id="myFutureTrips"
                     :items="futureTrips"
                     :fields="fields"
                     :per-page="perPageUpcoming"
                     :current-page="currentPageUpcoming"
            >
            </b-table>
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="numUpcomingtems-field"
                            label-for="perPageUpcoming">
                        <b-form-select id="perPageUpcoming" v-model="perPageUpcoming" :options="optionViews" size="sm" trim> </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            v-model="currentPageUpcoming"
                            :total-rows="rowsUpcoming"
                            :per-page="perPageUpcoming"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            align="center"
                            size="sm"
                    ></b-pagination>
                </b-col>
            </b-row>
        </div>

        <div id="pastTrips">
            <h1 class="page_title">Past Trips</h1>
            <p class="page_title"><i>Here are your past trips!</i></p>
            <b-table hover striped outlined
                     id="myPastTrips"
                     :items="pastTrips"
                     :fields="fields"
                     :per-page="perPagePast"
                     :current-page="currentPagePast"
                     :sort-by="sortBy"
                     :sort-desc="false"
            >
            </b-table>
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="numItemsPast-field"
                            label-for="perPagePast">
                        <b-form-select id="perPage" v-model="perPagePast" :options="optionViews" size="sm" trim> </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            v-model="currentPagePast"
                            :total-rows="rowsPast"
                            :per-page="perPagePast"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            align="center"
                            size="sm"
                    ></b-pagination>
                </b-col>
            </b-row>
        </div>
        <b-button v-b-toggle.collapsePlan class="mr-1">Wanting to Plan a Trip?</b-button>
        <b-collapse id="collapsePlan" style="margin-top: 20px">
            <plan-a-trip></plan-a-trip>
        </b-collapse>



    </div>

</template>

<script>
    import PlanATrip from './planATrip.vue'
    export default {
        name: "YourTrips",
        data: function() {
            return {
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPageUpcoming: 5,
                perPagePast: 5,
                currentPageUpcoming: 1,
                currentPagePast: 1,
                sortBy: 'start_date',
                fields: ['trip_name', 'start_date', 'end_date', 'more_details'],
                futureTrips: [
                    { id: 1, trip_name: 'France', start_date: '12/02/15', end_date: '15/02/15'},
                    { id: 2, trip_name: 'Germany', start_date: '15/02/15', end_date: '17/02/15'},
                    { id: 3, trip_name: 'Italy', start_date: '17/02/15', end_date: '19/02/15' },
                    { id: 4, trip_name: 'Greece', start_date: '19/02/15', end_date: '21/02/15' },
                    { id: 5, trip_name: 'Britain', start_date: '21/02/15', end_date: '23/02/15' },
                    { id: 6, trip_name: 'Fiji', start_date: '23/02/15', end_date: '25/02/15' },
                    { id: 7, trip_name: 'USA', start_date: '25/02/15', end_date: '27/02/15' },
                    { id: 8, trip_name: 'Australia', start_date: '27/02/15', end_date: '01/03/15' },
                    { id: 9, trip_name: 'NZ', start_date: '01/03/15', end_date: '03/03/15' }
                ],
                pastTrips: [
                    { id: 1, trip_name: 'France', start_date: '12/02/15', end_date: '15/02/15'},
                    { id: 2, trip_name: 'Germany', start_date: '15/02/15', end_date: '17/02/15'},
                    { id: 3, trip_name: 'Italy', start_date: '17/02/15', end_date: '19/02/15' },
                    { id: 4, trip_name: 'Greece', start_date: '19/02/15', end_date: '21/02/15' },
                    { id: 5, trip_name: 'Britain', start_date: '21/02/15', end_date: '23/02/15' },
                    { id: 6, trip_name: 'Fiji', start_date: '23/02/15', end_date: '25/02/15' },
                    { id: 7, trip_name: 'USA', start_date: '25/02/15', end_date: '27/02/15' },
                    { id: 8, trip_name: 'Australia', start_date: '27/02/15', end_date: '01/03/15' },
                    { id: 9, trip_name: 'NZ', start_date: '01/03/15', end_date: '03/03/15' }
                ]
            }

        },
        computed: {
            rowsUpcoming() {
                return this.futureTrips.length
            },
            rowsPast() {
                return this.pastTrips.length
            }
        },
        components: {
            PlanATrip
        }
    }
</script>

<style scoped>

    #pastTrips {
        margin-top: 20px;
        padding-top: 20px;
        border-top: 1px solid black;
    }
</style>