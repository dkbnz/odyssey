<template>
    <div>
        <b-form-file multiple :file-name-formatter="formatNames" accept="image/*"
                v-model="files"
                :state="Boolean(files)"
                placeholder="Choose files..."
                drop-placeholder="Drop files here..."
        ></b-form-file>
        <b-button class="btn btn-info btn-lg" @click="files = null">Clear Selected Files</b-button>
        <b-button class="btn btn-info btn-lg" @click="save()">Save</b-button>
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
                let self = this;
                console.log(self.files[0]);
                fetch(`/v1/savePhotos`, {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify({'photos': self.files})

                }).then(function(response) {
                    self.files = null;
                })
            }
        }
    }
</script>

<style scoped>

</style>