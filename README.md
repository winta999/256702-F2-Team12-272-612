# 256702-F2-Team-272-400
# Java Project
# แบบข้อเสนอโครงงาน
# Project Centipede Game 

# สมาชิก
1.	นายนัฐทิพงศ์  อ่อนจันทร์    รหัสนิสิต	6730300272
2.	นายพลภัทร	นภาวรรณ	    รหัสนิสิต	6730300400

# รายละเอียดโดยย่อ

# บทนำ

    โครงงาน Centipede Game เป็นการพัฒนาเกมแนวอาร์เคดที่ได้รับแรงบันดาลใจจากเกม Centipede ต้นฉบับซึ่งได้รับความนิยมในยุค 1980s เกมนี้ถูกออกแบบขึ้นมาโดยใช้ภาษา Java และพัฒนาโดยใช้ FXGL Framework ซึ่งเป็นเครื่องมือที่ช่วยให้การพัฒนาเกมเป็นไปอย่างสะดวกมากขึ้น ในเกมนี้ผู้เล่นจะได้รับบทบาทเป็นนักยิงที่สามารถเคลื่อนที่ไปทางซ้ายและขวาพร้อมทั้งยิงกระสุนเพื่อกำจัดศัตรูที่มีลักษณะคล้ายตะขาบ (Centipede) ซึ่งเคลื่อนตัวลงมาจากด้านบนของหน้าจอ ผู้เล่นจะต้องจัดการกับอุปสรรคต่างๆ ที่ขวางทางและพยายามทำคะแนนให้ได้มากที่สุดเพื่อให้สามารถผ่านไปยังด่านต่อไปได้
    เกม Centipede Game ได้รับการพัฒนาให้มีระบบการเล่นที่เข้าใจง่ายแต่ท้าทาย มีองค์ประกอบที่ช่วยเพิ่มความสนุกและความตื่นเต้น เช่น การเพิ่มระดับความยากในแต่ละด่าน ระบบการบันทึกคะแนนสูงสุด และการเก็บไอเทมพิเศษ ซึ่งช่วยเพิ่มประสิทธิภาพของตัวละครและเสริมความสามารถในการเล่น เกมนี้สามารถเล่นได้บนแพลตฟอร์มเดสก์ท็อปและออกแบบมาให้รองรับการควบคุมผ่านคีย์บอร์ด ซึ่งช่วยให้ผู้เล่นสามารถควบคุมการเคลื่อนไหวและการโจมตีได้อย่างมีประสิทธิภาพ

# วัตถุประสงค์ของโครงงาน

    การพัฒนา Centipede Game มีเป้าหมายเพื่อฝึกฝนทักษะการพัฒนาเกมในรูปแบบ 2D โดยใช้ภาษา Java และ FXGL Framework นอกจากนี้ยังต้องการสร้างเกมที่มีระบบการเล่นที่สมบูรณ์แบบและมีความท้าทายสำหรับผู้เล่น อีกทั้งยังมุ่งเน้นการพัฒนาเกมที่สามารถบันทึกและโหลดข้อมูลได้ เพื่อให้ผู้เล่นสามารถเล่นต่อจากจุดที่ค้างไว้ได้และสามารถแข่งขันกับตัวเองหรือผู้เล่นคนอื่นผ่านระบบคะแนนสูงสุด การศึกษาการออกแบบกลไกเกมและการจัดการองค์ประกอบภายในเกมถือเป็นหนึ่งในเป้าหมายสำคัญของโครงงานนี้

# รายละเอียดของโครงงาน

    Centipede Game ถูกออกแบบให้มีลักษณะเป็นเกมที่เล่นได้อย่างต่อเนื่อง โดยในแต่ละด่านจะมีระดับความยากที่เพิ่มขึ้น ศัตรูจะเคลื่อนที่ลงมาเรื่อย ๆ และมีพฤติกรรมที่แตกต่างกันไปตามระดับของเกม อุปสรรคในเกมจะมาในรูปแบบของเห็ด (Mushroom) ที่สามารถขวางทางและเปลี่ยนทิศทางการเคลื่อนที่ของ Centipede ได้ นอกจากนี้ยังมีไอเทมพิเศษ เช่น กระสุนพิเศษที่มีพลังโจมตีสูงขึ้น หรือโล่ป้องกันที่จะช่วยให้ผู้เล่นรอดจากการถูกโจมตีได้นานขึ้น ระบบคะแนนและการบันทึกคะแนนสูงสุดช่วยให้เกมมีความท้าทายและกระตุ้นให้ผู้เล่นพยายามทำคะแนนให้ได้มากที่สุด ระบบการควบคุมภายในเกมจะใช้คีย์บอร์ดเป็นหลัก ผู้เล่นสามารถเคลื่อนที่ไปทางซ้ายและขวา รวมถึงสามารถยิงกระสุนขึ้นไปด้านบนเพื่อโจมตีศัตรู หากศัตรูถูกยิงจนหมด ผู้เล่นจะสามารถผ่านไปยังด่านต่อไปที่มีความยากเพิ่มขึ้น เกมนี้ยังมีระบบพลังชีวิตที่ช่วยให้ผู้เล่นสามารถเล่นได้นานขึ้นและมีโอกาสแก้ตัวหากพลาดพลั้งในระหว่างการเล่น

# การแบ่งงานของสมาชิก

    โครงงานนี้มีสมาชิกทั้งหมดสองคน โดยแต่ละคนมีหน้าที่ดังต่อไปนี้:
        นายพลภัทร นภาวรรณ : รับผิดชอบการออกแบบและพัฒนาระบบการควบคุมของผู้เล่น    รวมถึงการจัดการกับการเคลื่อนที่ของตัวละครและระบบการยิงกระสุน รวมถึงทำเอกสาร และทดสอบระบบ

        นายนัฐทิพงศ์ อ่อนจันทร์ : ดูแลการพัฒนากลไกการเคลื่อนที่ของศัตรู รวมถึงการออกแบบระบบอุปสรรคต่าง ๆ และพัฒนา UI ให้ใช้งานได้ง่ายและเหมาะสมกับเกม จัดการ GitHub repository

# แผนการดำเนินงานระยะเวลา 8 สัปดาห์

    การดำเนินงานของโครงงานนี้ถูกแบ่งออกเป็น 8 สัปดาห์ โดยมีรายละเอียดของแต่ละช่วงเวลาดังต่อไปนี้:
        สัปดาห์ที่ 1-2: ศึกษาโครงสร้างของ FXGL Framework และเริ่มต้นสร้างโครงสร้างพื้นฐานของเกม รวมถึงการกำหนดขอบเขตของโครงการ
        สัปดาห์ที่ 3-4: พัฒนา Core Gameplay ซึ่งรวมถึงการควบคุมตัวละครผู้เล่น ระบบการยิง และการเคลื่อนที่ของศัตรู
        สัปดาห์ที่ 5-6: เพิ่มฟีเจอร์ต่าง ๆ เช่น ระบบคะแนน ไอเทมพิเศษ และปรับสมดุลของเกมให้เหมาะสม
        สัปดาห์ที่ 7: ทดสอบและแก้ไขข้อผิดพลาดของเกม รวมถึงการปรับปรุง UI ให้สมบูรณ์
        สัปดาห์ที่ 8: เตรียมเอกสารโครงการ ทำการสรุปและเตรียมการนำเสนอผลงาน

# ความท้าทายและแนวทางการแก้ไข

    ในกระบวนการพัฒนาเกม Centipede Game ทีมผู้พัฒนาได้พบกับความท้าทายหลายประการ หนึ่งในปัญหาหลักคือการใช้ FXGL Framework ซึ่งเป็นเครื่องมือที่สมาชิกในทีมยังไม่มีประสบการณ์มากนัก เพื่อแก้ไขปัญหานี้ ทีมงานได้ศึกษาเอกสารและตัวอย่างจาก FXGL Wiki รวมถึงทดลองเขียนโค้ดเพื่อทำความเข้าใจกลไกของเฟรมเวิร์ก ปัญหาอีกประการหนึ่งคือการควบคุมความเร็วของศัตรูและการเพิ่มระดับความยากให้เหมาะสม ซึ่งต้องใช้การปรับค่าต่าง ๆ และการทดสอบอย่างต่อเนื่องเพื่อให้มั่นใจว่าการเล่นเกมมีความท้าทายแต่ไม่ยากเกินไป

# สรุปและผลลัพธ์ที่คาดหวัง

    Centipede Game เป็นโครงงานที่มีเป้าหมายในการสร้างเกมที่สามารถเล่นได้อย่างสนุกสนานและท้าทาย โดยใช้ภาษา Java และ FXGL Framework เกมนี้ถูกออกแบบให้มีระบบที่สมบูรณ์แบบทั้งในด้านการเล่น ระบบคะแนน และการบันทึกข้อมูล สมาชิกในทีมได้รับประสบการณ์ในการพัฒนาเกมและการทำงานร่วมกันผ่านระบบ GitHub ซึ่งช่วยให้การพัฒนาเป็นไปอย่างราบรื่นและมีประสิทธิภาพ
    ผลลัพธ์ที่คาดหวังจากโครงงานนี้คือเกมที่สามารถเล่นได้อย่างลื่นไหล มีระบบคะแนนที่ท้าทาย และสามารถพัฒนาเพิ่มเติมได้ในอนาคต อีกทั้งสมาชิกในทีมจะได้รับทักษะที่สำคัญในการพัฒนาเกมและการทำงานเป็นทีม ซึ่งเป็นประโยชน์อย่างมากสำหรับการพัฒนาต่อไปในอนาคต

# แหล่งข้อมูลอ้างอิง

•	FXGL Framework: https://github.com/AlmasB/FXGL/wiki/FXGL-11
•	JavaFX Documentation: https://openjfx.io/
•	แนวทางการพัฒนาเกม Arcade: https://gamedevelopment.tutsplus.com/

