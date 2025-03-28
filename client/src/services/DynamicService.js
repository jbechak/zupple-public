import axios from "axios";
import { isReactive, unref } from "vue";
import { useQuery } from "vue-query";

// const apiBase = 'http://localhost:8080';

export default class DynamicService {
  static async get(id, serviceRoot) {
    if (!id) throw new Error("'id' must be specified");
    if (!serviceRoot) throw new Error("'serviceRoot' must be specified");
    const result = (await axios.get(`${serviceRoot}/${id}`));
    return result;
  }
  
  static useGet(id, serviceRoot, options) {
    if (options != null && !isReactive(options))    
      throw new Error("'options' must be either reactive or null");

    return useQuery([this.serviceRoot], async () => {
      return (await this.get(unref(id), serviceRoot)).data;
    }, options);
  }

  static async getByUser(userId, serviceRoot) {
    if (!userId) throw new Error("'userId' must be specified");
    if (!serviceRoot) throw new Error("'serviceRoot' must be specified");
    const result = (await axios.get(`${serviceRoot}/getByUser/${userId}`));
    return result;
  }
  
  static async getAll(serviceRoot) {
    if (!serviceRoot) throw new Error("'serviceRoot' must be specified");
    const result = (await axios.get(serviceRoot));
    return result;
  }

  static useGetAll(serviceRoot, options) {
    if (options != null && !isReactive(options))    
      throw new Error("'options' must be either reactive or null");

    return useQuery([this.serviceRoot], async () => {
      return (await this.getAll(serviceRoot)).data;
    }, options);
  }
    
  static async create(serviceRoot, requestObject) {
    if (!serviceRoot) throw new Error("'serviceRoot' must be specified");
    if (!requestObject) throw new Error("'requestObject' must be specified");
    const result = (await axios.post(serviceRoot, requestObject)).data;
    return result;
  }
  
  static async update(serviceRoot, requestObject) {
    if (!serviceRoot) throw new Error("'serviceRoot' must be specified");
    if (!requestObject) throw new Error("'requestObject' must be specified");
    const result = (await axios.put(serviceRoot, requestObject)).data;
    return result;
  }
  
  static async delete(id, serviceRoot) {
    if (!id) throw new Error("'id' must be specified");
    if (!serviceRoot) throw new Error("'serviceRoot' must be specified");
    const result = (await axios.delete(`${serviceRoot}/${id}`));
    return result;
  }
  
  static async generate(serviceRoot, requestObject) {
    if (!serviceRoot) throw new Error("'serviceRoot' must be specified");
    if (!requestObject) throw new Error("'requestObject' must be specified");
    const result = (await axios.post(`${serviceRoot}/generate`, requestObject)).data;
    return result;
  }
}