<template>
    <div class="container">
        <h1 class="page_title">{{ heading }}</h1>
        <p class="page_title"><i>Book your next trip!</i></p>
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
                 :items="items"
                 :fields="fields"
                 :per-page="perPage"
                 :current-page="currentPage"
        >
            <template slot="show_details" slot-scope="row">
                <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                    {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                </b-button>
            </template>
            <template slot="delete" slot-scope="row">
                <b-button size="sm" @click="row.deleteDestination(row)" class="mr-2">Delete
                </b-button>
            </template>
            <template slot="row-details" slot-scope="row">
                <b-card>

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

    </div>
</template>

<script>
    export default {
        name: "PlanATrip",
        created() {
            document.title = "TravelEA - Plan a Trip";
        },
        data() {
            return {
                heading: 'Plan a Trip',
                optionViews: [{value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPage: 10,
                currentPage: 1,
                tripName: "",
                destinationName: "",
                inDate: "",
                outDate: "",
                fields: [
                    { key: 'destination_name'},
                    { key: 'in_date' },
                    { key: 'out_date' },
                    'show_details',
                    'delete'
                ],
                subFields: [
                    {key: 'destinationList'},
                    {key: 'inDate'},
                    {key: 'outDate'}
                ],
                items: [
                    { id: 1, destination_name: 'France', in_date: '12/02/15', out_date: '15/02/15'},
                    { id: 2, destination_name: 'German', in_date: '15/03/15', out_date: '17/02/15'},
                    { id: 3, destination_name: 'Italy', in_date: '17/02/15', out_date: '19/02/15' },
                    { id: 4, destination_name: 'Greece', in_date: '19/02/15', out_date: '21/02/15' },
                    { id: 5, destination_name: 'Britain', in_date: '21/02/15', out_date: '23/02/15' },
                    { id: 6, destination_name: 'Fiji', in_date: '23/02/15', out_date: '25/02/15' },
                    { id: 7, destination_name: 'USA', in_date: '25/02/15', out_date: '27/02/15' },
                    { id: 8, destination_name: 'Australia', in_date: '27/02/15', out_date: '01/03/15' },
                    { id: 9, destination_name: 'NZ', in_date: '01/03/15', out_date: '03/03/15' }
                ],

            }
        },
        computed: {
            rows() {
                return this.items.length
            }
        },
        methods: {
            addDestination: function() {
                this.items.push({destination_name: this.destinationName,in_date: this.inDate,out_date: this.outDate});
                console.log(this.tripName, this.destinationName, this.inDate, this.outDate);
            },
            deleteDestination: function(index) {
                this.items.splice(index, 1)
            }
        }
    }
</script>

<style scoped>

</style>