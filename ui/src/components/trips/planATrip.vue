<template>
    <div class="container">
        <h1 class="page_title">{{ heading }}</h1>
        <p class="page_title"><i>Book your next trip!</i></p>
        <b-alert v-model="nameAlert" variant="danger" dismissible>No Trip Name!</b-alert>
        <b-alert v-model="destinationsAlert" variant="danger" dismissible>Must be at least 2 destinations</b-alert>

        <b-alert
                :show="dismissCountDown"
                dismissible
                variant="success"
                @dismissed="dismissCountDown=0"
                @dismiss-count-down="countDownChanged"
        >
            <p>Trip Successfully Added</p>
            <b-progress
                    variant="success"
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
            ></b-progress>
        </b-alert>

        <b-container fluid >
            <b-form-group
                    id="trip_name-field"
                    label="Trip Name:"
                    label-for="trip_name">
                <b-form-input id="trip_name" v-model="tripName" :type="'text'" trim></b-form-input>
            </b-form-group>
        </b-container>
        <b-container>
            <b-row>
                <b-col>
                    <b-form-group
                            id="destination-field"
                            label="Add a Destination:"
                            label-for="destination">
                        <b-form-input id="destination" v-model="destinationName" :type="'text'" trim></b-form-input>
                    </b-form-group>
                </b-col>
                <b-col>
                    <b-form-group
                            id="inDate-field"
                            label="In Date (optional):"
                            label-for="inDate">
                        <b-form-input id="inDate" v-model="inDate" :type="'date'" trim></b-form-input>
                    </b-form-group>
                </b-col>
                <b-col>
                    <b-form-group
                            id="outDate-field"
                            label="Out Date (optional):"
                            label-for="outDate">
                        <b-form-input id="outDate" v-model="outDate" :type="'date'" trim></b-form-input>
                    </b-form-group>
                </b-col>
            </b-row>
            <b-button class="mr-2 float-right" @click="addDestination">Add Destination</b-button>
        </b-container>

        <b-table hover striped outlined
                 id="myTrips"
                 :items="destinations"
                 :fields="fields"
                 :per-page="perPage"
                 :current-page="currentPage"
        >
            <template slot="actions" slot-scope="row">
                <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                    {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                </b-button>
                <b-button size="sm" @click="deleteDestination(row.item.id)" variant="danger" class="mr-2">Delete
                </b-button>
            </template>
            <template slot="row-details" slot-scope="row">
                <b-card>
                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Type:</b></b-col>
                        <b-col>{{ row.item.type }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>District:</b></b-col>
                        <b-col>{{ row.item.district }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Latitude:</b></b-col>
                        <b-col>{{ row.item.latitude }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Longitude:</b></b-col>
                        <b-col>{{ row.item.longitude }}</b-col>
                    </b-row>

                    <b-row class="mb-2">
                        <b-col sm="3" class="text-sm-right"><b>Country:</b></b-col>
                        <b-col>{{ row.item.country }}</b-col>
                    </b-row>

                </b-card>
            </template>
        </b-table>
        <b-row>
            <b-col cols="1">
                <b-form-group
                        id="numItems-field"
                        label-for="perPage">
                    <b-form-select id="perPage" v-model="perPage" :options="optionViews" size="sm" trim> </b-form-select>
                </b-form-group>
            </b-col>
            <b-col cols="8">
                <b-pagination
                        v-model="currentPage"
                        :total-rows="rows"
                        :per-page="perPage"
                        aria-controls="my-table"
                        first-text="First"
                        last-text="Last"
                        align="center"
                        size="sm"
                ></b-pagination>
            </b-col>
        </b-row>
        <b-row>
            <b-button @click="submitTrip">Save Trip</b-button>
        </b-row>

    </div>
</template>

<script>
    export default {
        name: "PlanATrip",
        data() {
            return {
                heading: 'Plan a Trip',
                optionViews: [{value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPage: 10,
                currentPage: 1,
                tripName: null,
                destinationName: "",
                inDate: "",
                outDate: "",
                nameAlert: false,
                destinationsAlert: false,
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                fields: [
                    { key: 'destination_name'},
                    { key: 'in_date' },
                    { key: 'out_date' },
                    'actions'
                ],
                subFields: [
                    {key: 'type'},
                    {key: 'district'},
                    {key: 'latitude'},
                    {key: 'longitude'},
                    {key: 'country'},


                ],
                destinations: [
                    { id: 1, destination_name: '15 Mile Creek', in_date: '12/02/15', out_date: '15/02/15', type: "STREAM", district: "Nelson", latitude: "-40.79825", longitude: "172.514222", country: "New Zealand"},

                ],

            }
        },
        computed: {
            rows() {
                return this.destinations.length
            }
        },
        methods: {
            addDestination: function() {
                this.destinations.push({destination_name: this.destinationName,in_date: this.inDate,out_date: this.outDate});
            },
            deleteDestination: function(id) {
                console.log(id);
                this.destinations.splice(id, 1)
            },
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },
            submitTrip: function() {
                if (this.tripName === null || this.tripName.length === 0) {
                    this.destinationsAlert = false;
                    this.nameAlert = true;
                } else if (this.destinations.length < 2) {
                    this.nameAlert = false;
                    this.destinationsAlert = true;
                } else {
                    this.nameAlert = false;
                    this.destinationsAlert = false;
                    this.dismissCountDown = this.dismissSecs;
                    let trip = {
                        name: this.tripName,
                        tripDestinations: this.destinations
                    };
                    console.log(trip);
                }

            }
        }
    }
</script>

<style scoped>

</style>