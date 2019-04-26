<template>
    <div class="container">
        <div id="upcomingTrips">
            <h1 class="page_title">Your Upcoming Trips</h1>
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
            <h1 class="page_title">Your Past Trips</h1>
            <p class="page_title"><i>Here are your past trips!</i></p>
            <b-table hover striped outlined
                     id="myPastTrips"
                     :items="pastTrips"
                     :fields="fields"
                     :per-page="perPagePast"
                     :current-page="currentPagePast"
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
    export default {
        name: "YourTrips",
        data: function() {
            return {
                optionViews: [{value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPageUpcoming: 5,
                perPagePast: 5,
                currentPageUpcoming: 1,
                currentPagePast: 1,
                futureTrips: [
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
                pastTrips: [
                    { id: 1, destination_name: 'France', in_date: '12/02/15', out_date: '15/02/15'},
                    { id: 2, destination_name: 'German', in_date: '15/03/15', out_date: '17/02/15'},
                    { id: 3, destination_name: 'Italy', in_date: '17/02/15', out_date: '19/02/15' },
                    { id: 4, destination_name: 'Greece', in_date: '19/02/15', out_date: '21/02/15' },
                    { id: 5, destination_name: 'Britain', in_date: '21/02/15', out_date: '23/02/15' },
                    { id: 6, destination_name: 'Fiji', in_date: '23/02/15', out_date: '25/02/15' },
                    { id: 7, destination_name: 'USA', in_date: '25/02/15', out_date: '27/02/15' },
                    { id: 8, destination_name: 'Australia', in_date: '27/02/15', out_date: '01/03/15' },
                    { id: 9, destination_name: 'NZ', in_date: '01/03/15', out_date: '03/03/15' }
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
    }
</script>

<style scoped>

    #pastTrips {
        margin-top: 20px;
        padding-top: 20px;
        border-top: 1px solid black;
    }
</style>