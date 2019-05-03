<template>
    <div>
        <b-form-file multiple :file-name-formatter="formatNames" accept="image/*"
                v-model="files"
                :state="Boolean(files)"
                placeholder="Choose files..."
                drop-placeholder="Drop files here..."
        ></b-form-file>
        <b-button @click="files = null">Clear Selected Files</b-button>
    </div>
</template>

<script>
    export default {
        name: "photoUploader",
        data: function() {
            return {
                files: null
            }
        },
        methods: {
            formatNames(files) {
                if (files.length === 1) {
                    return files[0].name
                } else {
                    return `${files.length} images selected`
                }
            },
            convertFiles(files){
                //conversion from array of file objects to desired form for backend sending?
            },
            save(){
                //will close modal and send files away to backend?
            }
        }
    }
</script>

<style scoped>

</style>