<template>
    <div>
        <h1 class="page_title">{{ heading }}</h1>

        <b-table striped hover
                 id="myTrips"
                 :items="items"
                 :fields="fields"
                 :per-page="perPage"
                 :current-page="currentPage"
                 :sort-by.sync="sortBy"
                 :sort-desc.sync="sortDesc"
        >
            <template slot="show_details" slot-scope="row">
                <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                    {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                </b-button>
            </template>
            <template slot="row-details" slot-scope="row">
                <b-card>
                    <b-table id="tripDetails" :items="items" :fields="subFields">
                    </b-table>
                </b-card>
            </template>
        </b-table>
        <b-pagination
                v-model="currentPage"
                :total-rows="rows"
                :per-page="perPage"
                aria-controls="my-table"
                first-text="First"
                last-text="Last"
                size="sm"
                align="center"
        ></b-pagination>
    </div>

</template>

<script>
    export default {
        name: "Trips",
        data() {
            return {
                heading: 'Trips',
                perPage: 5,
                currentPage: 1,
                fields: [
                    { key: 'trip_name', sortable: true },
                    { key: 'start_date', sortable: true },
                    { key: 'end_date', sortable: true },
                    { key: 'age', sortable: true },
                    'show_details'
                ],
                subFields: [
                    {key: 'destinationList'},
                    {key: 'inDate'},
                    {key: 'outDate'}
                ],
                items: [
                    { id: 1, trip_name: 'France', start_date: '12/02/15', destinationList: ['PARIS']},
                    { id: 2, trip_name: 'German', start_date: '12/02/15' },
                    { id: 3, trip_name: 'Italy', start_date: '12/02/15' },
                    { id: 4, trip_name: 'Greece', start_date: '12/02/15' },
                    { id: 5, trip_name: 'Britain', start_date: '12/02/15' },
                    { id: 6, trip_name: 'Fiji', start_date: '12/02/15' },
                    { id: 7, trip_name: 'USA', start_date: '12/02/15' },
                    { id: 8, trip_name: 'Australia', start_date: '12/02/15' },
                    { id: 9, trip_name: 'NZ', start_date: '12/02/15' }
                ]
            }
        },
        computed: {
            rows() {
                return this.items.length
            }
        }
    }
</script>

<style scoped>

</style>