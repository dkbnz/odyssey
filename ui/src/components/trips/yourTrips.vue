<template>
    <div class="container">
        <div id="upcomingTrips">
            <h1 class="page_title">Upcoming Trips</h1>
            <p class="page_title"><i>Here are your upcoming trips!</i></p>
            <b-table hover striped outlined
                     id="myFutureTrips"
                     ref="myFutureTrips"
                     :items="trips"
                     :fields="fields"
                     :per-page="perPageUpcoming"
                     :current-page="currentPageUpcoming"
            >
            <template slot="more_details" slot-scope="row">
                <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                    {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                </b-button>
            </template>
            <template slot="row-details" slot-scope="row">
                <b-card>
                    <b-table
                    id="futureTripsDestinations"
                    :items="row.item.destinations"
                    :fields="subFields">
                    </b-table>

                </b-card>
            </template>
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
                     ref="myPastTrips"
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


    </div>

</template>

<script>
    import PlanATrip from './planATrip.vue'
    export default {
        name: "YourTrips",
        props: ['profile'],
        data: function() {
            return {
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPageUpcoming: 5,
                perPagePast: 5,
                currentPageUpcoming: 1,
                currentPagePast: 1,
                sortBy: 'destinations[0].startDate',
                fields: ['name', {key:'destinations[0].startDate', label: 'Start Date'}, {key:'destinations[1].endDate', label: 'End Date'}, 'more_details'],
                subFields:[
                    {key: 'destination.name', label: "Destination Name"},
                    {key: 'destination.type.destinationType', label: "Destination Type"},
                    {key: 'destination.district', label: "Destination District"},
                    {key: 'destination.latitude', label: "Destination Latitude"},
                    {key: 'destination.longitude', label: "Destination Longitude"},
                    {key: 'destination.startDate', label: "In Date"},
                    {key: 'destination.endDate', label: "Out Date"}],
                pastTrips: [],
                trips:[]
            }

        },
        mounted () {
            this.getAllTrips(trips => this.trips = trips);
        },
        computed: {
            rowsUpcoming() {
                return this.trips.length
            },
            rowsPast() {
                return this.pastTrips.length
            }
        },
        components: {
            PlanATrip
        },
        methods: {
            getAllTrips(cb) {
                let userId = this.profile.id;
                return fetch(`/v1/trips/all?id=` + userId, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },
            checkStatus (response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error); // eslint-disable-line no-console
                throw error;
            },
            parseJSON (response) {
                return response.json();
            },
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