<template>
  <div>
    <div class="card" style="font-size: large; padding-left: 10px">
      您好{{ user?.username }}，欢迎使用本系统!
    </div>
<!--    <el-row style="height: 40vh">-->
<!--      <el-col style="text-align: center" :span="24">-->
<!--        <el-col :span="24">-->
<!--          <div class="block">-->
<!--            <el-carousel height="300px" >-->
<!--              <el-carousel-item v-for="item in 4" :key="item">-->
<!--                <h3 class="small">{{ item }}</h3>-->
<!--              </el-carousel-item>-->
<!--            </el-carousel>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-col>-->
<!--    </el-row>-->
    <el-row :gutter=10>
      <div style="display: flex; margin: 10px 0">
        <div style="width: 50%;" class="card">
          <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">公告列表</div>

<!--          左边的公告列表-->
         <el-col :span="12">
             <div>
               <el-timeline  reverse slot="reference">
                 <el-timeline-item v-for="item in notices" :key="item.announcementId" :timestamp="item.announcementTime">
                   <el-popover
                       placement="right"
                       width="200"
                       trigger="hover"
                       :content="item.announcementText">
                     <span slot="reference">{{ item.announcementTitle }}</span>
                   </el-popover>
                 </el-timeline-item>
               </el-timeline>
             </div>
         </el-col>
        </div>

      </div>
    </el-row>
  </div>
</template>

<script>

export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: []
    }
  },
  created() {
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 150px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>