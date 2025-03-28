<template>
  <div class="table-responsive">
    <table class="table table-striped table-hover">
      <thead>
        <tr>
          <th v-for="col in columns" :key="col.header" :class="getHeaderClass(col)">{{ col.header }}</th>
          <!-- <th class="fw-bold" scope="col">Title</th>
          <th class="fw-bold text-end" scope="col">Words</th>
          <th class="fw-bold text-end" scope="col">Width</th>
          <th class="fw-bold text-end" scope="col">Height</th> -->
          <th class=""></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(record, index) in listingData" :key="index" class="">
          <td v-for="col in columns" :key="col.field" :class="getFieldClass(col)">{{ record[col.field] }}</td>

          <!-- <td>{{ record.title }}</td>
          <td class="text-end">{{ Object.keys(record.wordClues)?.length }}</td>
          <td class="text-end">{{ record.width }}</td>
          <td class="text-end">{{ record.height }}</td> -->
          <ListTableButtons
            class=""
            :record="record"
            @edit="emit('edit', record)"
            @view="emit('view', record)"
            @delete="emit('delete', record)"
          />
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import ListTableButtons from '@/components/ListTableButtons.vue';
import { STYLES } from '@/constants';

const props = defineProps({
  listingData: { type: Array, default: [] },
  columns: { type: Array, default: [] },
});
const emit = defineEmits(['edit', 'view', 'delete']);

const getHeaderClass = (col) => col.isNumber ? [STYLES.TEXT_END, STYLES.FW_BOLD] : STYLES.FW_BOLD;
const getFieldClass = (col) => col.isNumber ? STYLES.TEXT_END : null;
</script>