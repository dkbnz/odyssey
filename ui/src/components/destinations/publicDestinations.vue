<template>
    <div>
        <div v-if="searchDestinationForm">
            <b-list-group>
                <b-list-group-item class="flex-column align-items-start">
                    <search-destinations v-if="searchDestinationForm"
                                         :profile="profile"
                                         :destinationTypes="destinationTypes"
                                         @searched-destinations="showResults"
                    ></search-destinations>
                </b-list-group-item>
            </b-list-group>
        </div>
       <div v-else>
           <b-button variant="success" @click="searchDestinationForm = true" block>Search</b-button>
           <b-list-group class="scroll">
               <b-list-group-item v-for="destination in (publicDestinations)" href="#" class="flex-column align-items-start" @click="$emit('destination-click', destination)">
                   <div class="d-flex w-100 justify-content-between">
                       <h5 class="mb-1">{{destination.name}}</h5>
                   </div>

                   <p class="mb-1">
                       {{destination.district}}
                   </p>
                   <p class="mb-1">
                       {{destination.country}}
                   </p>

               </b-list-group-item>
           </b-list-group>
       </div>
</div>
</template>

<script>
import SearchDestinations from "./searchDestinations";
export default {
   name: "publicDestinations",
   components: {SearchDestinations},
   props: ['profile', 'destinationTypes'],
    data() {
       return {
           publicDestinations: [],
           searchDestinationForm: true
       }
    },
    methods: {
        /**
         * Displays results emitted from the search destinations components
         */
        showResults(destinations) {
            this.publicDestinations = destinations;
            this.searchDestinationForm = false;
        }
    }


}
</script>

<style scoped>

</style>